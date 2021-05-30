package com.jaz.movie.booking.service;

import com.jaz.movie.booking.config.MessagingConfig;
import com.jaz.movie.booking.config.PaytmConfig;
import com.jaz.movie.booking.dto.PaymentRequestDto;
import com.jaz.movie.booking.dto.PaymentStatus;
import com.jaz.movie.booking.entity.BookedTicket;
import com.jaz.movie.booking.entity.PartialTicketBook;
import com.jaz.movie.booking.entity.Payment;
import com.jaz.movie.booking.entity.User;
import com.jaz.movie.booking.exception.TicketTimeOutException;
import com.jaz.movie.booking.exception.UserNotFoundException;
import com.jaz.movie.booking.repository.*;
import com.jaz.movie.booking.utils.DateUtil;
import com.jaz.movie.booking.utils.PaymentConstants;
import com.paytm.pg.merchant.PaytmChecksum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PartialTicketBookRepository partialTicketBookRepository;

    @Autowired
    private BookedTicketRepository bookedTicketRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaytmConfig paytmConfig;

    @Value("${ticket.session.time.in.minutes}")
    private int ticketSession;

    @Autowired
    private AvailableShowsInfoRepository availableShowsInfoRepository;
    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Override
    public BookedTicket paymentValidationAndUpdatePayment(PaymentRequestDto paymentRequestDto, TreeMap<String, String> parameters) {

        //Check Valid User in DB
        Optional<User> userObject = userInfoRepository.findById(Long.valueOf(paymentRequestDto.getUserId()));
        if (!userObject.isPresent()) {
            log.info("User Not Found ::: " + userObject);
            throw new UserNotFoundException("User not found in out system!!!");
        }

        //Check if the ticket is already booked or not
        assertCheckTicketAlreadyBooked(paymentRequestDto);

        User user = userObject.get();
        paymentRequestDto.setUserName(user.getName());
        paymentRequestDto.setEmailId(user.getLoginId());

        //Update Payments object in Payment table
        Payment payment = savePayment(parameters);
        //Update Movie name and time
        paymentRequestDto.setMovieName(movieInfoRepository.findById(paymentRequestDto.getMovieId()).get().getName());
        paymentRequestDto.setMovieTime(availableShowsInfoRepository.findById(paymentRequestDto.getShowId()).get().getShowTimings());

        BookedTicket bookedTicket = saveBookedTicket(paymentRequestDto, payment.getId());

        // Update the partial_ticket_booking table
        updatePaymentInPartialTicketBookEntity(paymentRequestDto, bookedTicket.getBookedId());

        //Trigger Confirmation to Mail
        triggerEmail(bookedTicket);
        return bookedTicket;
    }

    /**
     * @param parameters
     */
    public Payment savePayment(TreeMap<String, String> parameters) {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(parameters.get(PaymentConstants.TXN_AMOUNT)));
        payment.setStatus(parameters.get(PaymentConstants.STATUS)
                .equalsIgnoreCase(PaymentConstants.PAYMENT_SUCCESS)
                ? PaymentStatus.SUCCESS.toString()
                : PaymentStatus.FAILURE.toString());
        payment.setBankName(parameters.get(PaymentConstants.BANK_NAME));
        payment.setGateWayName(parameters.get(PaymentConstants.GATEWAY_NAME));
        payment.setPaymentMode(parameters.get(PaymentConstants.PAYMENT_MODE));
        payment.setResponseCode(parameters.get(PaymentConstants.RESP_CODE));
        payment.setResponseMess(parameters.get(PaymentConstants.RESP_MSG));
        payment.setTransactionDate(parameters.get(PaymentConstants.TXN_DATE));
        payment.setTransactionDate(parameters.get(PaymentConstants.TXN_DATE));
        payment.setTransactionId(parameters.get(PaymentConstants.TRANSACTION_ID));
        payment.setBankTxnId(parameters.get(PaymentConstants.BANK_TRANSACTION_ID));
        return paymentRepository.save(payment);
    }


    /**
     * @param paymentRequestDto
     */
    private void assertCheckTicketAlreadyBooked(PaymentRequestDto paymentRequestDto) {
        PartialTicketBook partialTicketBook = partialTicketBookRepository.checkPaymentDoneOrNot(
                paymentRequestDto.getPartialBookId(), Boolean.TRUE);
        if (null != partialTicketBook) {
            log.info("Error : assertCheckTicketAlreadyBooked : " + paymentRequestDto.toString());
            throw new TicketTimeOutException("This Ticket " + partialTicketBook.getSeatId() + " is already booked.");
        }
    }

    /*    *//**
     * @param paymentRequestDto
     *//*
    private void assertCheckTicketValidation(PaymentRequestDto paymentRequestDto) {
        Optional<PartialTicketBook> partialTicketBook = partialTicketBookRepository.findById(paymentRequestDto.getPartialBookId());
        if (null != partialTicketBook) {
            log.info("Error : assertCheckTicketValidation : " + paymentRequestDto.toString());
            assertCheckTicketExpiryInMinutes(partialTicketBook.get());
        }
    }*/

    /**
     * @param partialTicketBook
     */
    private void assertCheckTicketExpiryInMinutesAfterPayment(PartialTicketBook partialTicketBook) {
        int minutes = DateUtil.findMinutes(partialTicketBook.getCreatedDate());
        if (ticketSession < minutes) {
            log.info("Error : assertCheckTicketExpiryInMinutes : " + partialTicketBook.toString());
            partialTicketBook.setIsActive(Boolean.TRUE);
            // Doing soft delete. User can book this ticket.
            partialTicketBookRepository.save(partialTicketBook);
            throw new TicketTimeOutException("This Ticket " + partialTicketBook.getSeatId() + " session is exceeded more than 2 minutes" +
                    " so please choose the seats one more time and Kindly make the payment within 2 minutes." +
                    " If the amount is reduced from your account we will return the amount within 7 business days.");
        }
    }

    /**
     * @param partialTicketBook
     */
    private void assertCheckTicketExpiryInMinutes(PartialTicketBook partialTicketBook) {
        int minutes = DateUtil.findMinutes(partialTicketBook.getCreatedDate());
        if (ticketSession < minutes) {
            log.info("Error : assertCheckTicketExpiryInMinutes : " + partialTicketBook.toString());
            partialTicketBook.setIsActive(Boolean.TRUE);
            // Doing soft delete. User can book this ticket.
            partialTicketBookRepository.save(partialTicketBook);
            throw new TicketTimeOutException("This Ticket " + partialTicketBook.getSeatId() + " session is exceeded more than 2 minutes" +
                    " so please choose the seats one more time and Kindly make the payment within 2 minutes.");
        }
    }

    /**
     * @param paymentRequestDto
     */
    private BookedTicket saveBookedTicket(PaymentRequestDto paymentRequestDto, Long paymentId) {
        BookedTicket bookedTicket = new BookedTicket();
        bookedTicket.setAmount(paymentRequestDto.getAmount());
        bookedTicket.setBookedSeats(paymentRequestDto.getTickets());
        bookedTicket.setMovieBookedDate(paymentRequestDto.getMovieDate());
        bookedTicket.setMovieId(paymentRequestDto.getMovieId());
        bookedTicket.setMovieName(paymentRequestDto.getMovieName());
        bookedTicket.setShowId(paymentRequestDto.getShowId());
        bookedTicket.setBookedId(paymentRequestDto.getPartialBookId());
        bookedTicket.setPaymentId(paymentId);
        bookedTicket.setMovieTime(paymentRequestDto.getMovieTime());
        bookedTicket.setNoOfPersons(paymentRequestDto.getNoOfPersons());
        bookedTicket.setScreenId(paymentRequestDto.getScreenId());
        bookedTicket.setUserName(paymentRequestDto.getUserName());
        bookedTicket.setEmailId(paymentRequestDto.getEmailId());
        bookedTicket.setPaymentStatus(PaymentStatus.SUCCESS.toString());
        return bookedTicketRepository.save(bookedTicket);
    }

    /**
     * @param paymentRequestDto
     */
    private void updatePaymentInPartialTicketBookEntity(PaymentRequestDto paymentRequestDto, Long bookedId) {
        List<Integer> bookedIds = partialTicketBookRepository.fetchPartialTicketBooksSeatsById(paymentRequestDto.getPartialBookId());
        bookedIds.stream().forEach(action -> {
            PartialTicketBook partialTicketBookDb = partialTicketBookRepository.findById(Long.valueOf(action)).get();
            partialTicketBookDb.setIsPaymentDone(Boolean.TRUE);
            partialTicketBookDb.setIsActive(Boolean.TRUE);
            partialTicketBookDb.setBookedTicketId(bookedId);
            partialTicketBookRepository.save(partialTicketBookDb);
        });
    }

    /**
     * @param ticket
     * @return
     */
    private void triggerEmail(BookedTicket ticket) {
        log.info("#TicketRequest Obj::" + ticket.toString());
        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, ticket);
        log.info("Email Triggered to: ", ticket.getEmailId());
    }


    public boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
        return PaytmChecksum.verifySignature(parameters,
                paytmConfig.getMerchantKey(), paytmChecksum);
    }


    public String getCheckSum(TreeMap<String, String> parameters) throws Exception {
        return PaytmChecksum.generateSignature(parameters, paytmConfig.getMerchantKey());
    }

    @Transactional
    public BookedTicket updatePayment(TreeMap<String, String> parameters, Long bookingId) {
        Optional<PartialTicketBook> partialTicketBookObj = partialTicketBookRepository.findByIdAndIsActiveFalse(bookingId);
        if (partialTicketBookObj.isPresent()) {
            //log.info("Error : assertCheckTicketValidation : " + paymentRequestDto.toString());
            assertCheckTicketExpiryInMinutesAfterPayment(partialTicketBookObj.get());
        } else {
            log.info("Ticket validation is expired!");
            throw new TicketTimeOutException("This Ticket session is exceeded more than 2 minutes" +
                    " so please choose the seats one more time and Kindly make the payment within 2 minutes." +
                    " If the amount is reduced from your account, we will return the amount within 7 business days.");
        }

        PartialTicketBook partialTicketBook = partialTicketBookObj.get();
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setAmount(new BigDecimal(parameters.get(PaymentConstants.TXN_AMOUNT)));
        paymentRequestDto.setMovieId(partialTicketBook.getMovieId());
        paymentRequestDto.setScreenId(partialTicketBook.getScreenId());
        paymentRequestDto.setShowId(partialTicketBook.getShowId());
        paymentRequestDto.setTickets(partialTicketBook.getSeats());
        paymentRequestDto.setPartialBookId(bookingId);
        paymentRequestDto.setNoOfPersons(partialTicketBook.getNoOfPersons());
        paymentRequestDto.setMovieDate(partialTicketBook.getMovieDate());
        paymentRequestDto.setUserId(partialTicketBook.getUserId());
        return paymentValidationAndUpdatePayment(paymentRequestDto, parameters);
    }

    /**
     * @param bookingId
     */
    public void assertCheckTicketSession(Long bookingId) {
        Optional<PartialTicketBook> partialTicketBookObj = partialTicketBookRepository.findByIdAndIsActiveFalse(bookingId);
        if (partialTicketBookObj.isPresent()) {
            assertCheckTicketExpiryInMinutes(partialTicketBookObj.get());
        } else {
            log.info("Ticket validation is expired!");
            throw new TicketTimeOutException("This Ticket session is exceeded more than 2 minutes" +
                    " so please choose the seats one more time and Kindly make the payment within 2 minutes.");
        }
    }
}
