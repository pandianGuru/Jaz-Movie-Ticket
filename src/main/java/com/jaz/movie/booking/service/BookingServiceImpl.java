package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.PartialTicketBookRequestDto;
import com.jaz.movie.booking.dto.PartialTicketBookResponseDto;
import com.jaz.movie.booking.entity.PartialTicketBook;
import com.jaz.movie.booking.entity.User;
import com.jaz.movie.booking.exception.BookingServiceException;
import com.jaz.movie.booking.exception.JwtExpiredException;
import com.jaz.movie.booking.exception.TicketNotAvailableException;
import com.jaz.movie.booking.exception.UserNotFoundException;
import com.jaz.movie.booking.repository.PartialTicketBookRepository;
import com.jaz.movie.booking.repository.UserInfoRepository;
import com.jaz.movie.booking.utils.DateUtil;
import com.jaz.movie.booking.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    PartialTicketBookRepository partialTicketBookRepository;

    @Value("${ticket.session.time.in.minutes}")
    private int ticketSession;

    @Override
    public PartialTicketBookResponseDto bookPartialTicket(HttpServletRequest httpServletRequest, PartialTicketBookRequestDto partialTicketBookRequestDto) {
        //General Validation
        assertDateValidation(partialTicketBookRequestDto);

        //check ticket is already partially booked or not
        assertCheckTicketBookedPartially(partialTicketBookRequestDto);

        //Update customer id
        Long userId = updateCustomerId(httpServletRequest, partialTicketBookRequestDto);

        PartialTicketBookResponseDto responseDto = new PartialTicketBookResponseDto();
        PartialTicketBook partialTicketBook = null;
        sortSeatsInAscOrder(partialTicketBookRequestDto);
        for (int i = 0; i < partialTicketBookRequestDto.getNoOfPersons(); i++) {
            partialTicketBook = new PartialTicketBook();
            partialTicketBook.setMovieId(partialTicketBookRequestDto.getMovieId());
            partialTicketBook.setShowId(partialTicketBookRequestDto.getShowId());
            partialTicketBook.setScreenId(partialTicketBookRequestDto.getScreenId());
            partialTicketBook.setNoOfPersons(partialTicketBookRequestDto.getNoOfPersons());
            partialTicketBook.setSeats(partialTicketBookRequestDto.getTickets());
            partialTicketBook.setSeatId(partialTicketBookRequestDto.getTickets().split(",")[i]);
            partialTicketBook.setUserId(userId);
            partialTicketBook.setCreatedDate(new Date());
            partialTicketBook.setMovieDate(partialTicketBookRequestDto.getMovieDate());
            partialTicketBook.setIsPaymentDone(Boolean.FALSE);
            partialTicketBook.setIsActive(Boolean.FALSE);
            try {
                partialTicketBook = partialTicketBookRepository.save(partialTicketBook);
            } catch (DataIntegrityViolationException ex) {
                throw new TicketNotAvailableException("This Ticket " + partialTicketBook.getSeatId() + " is already booked");
            }
        }

        responseDto.setMovieDate(partialTicketBook.getMovieDate());
        responseDto.setId(partialTicketBook.getId());
        responseDto.setIsPaymentDone(partialTicketBook.getIsPaymentDone());
        responseDto.setMovieId(partialTicketBook.getMovieId());
        responseDto.setNoOfPersons(partialTicketBook.getNoOfPersons());
        responseDto.setScreenId(partialTicketBook.getScreenId());
        responseDto.setShowId(partialTicketBook.getShowId());
        responseDto.setSeats(partialTicketBook.getSeats());
        responseDto.setUserId(partialTicketBook.getUserId());
        responseDto.setMovieTime(partialTicketBookRequestDto.getMovieTime());
        return responseDto;
    }

    /**
     * @param httpServletRequest
     * @param partialTicketBookRequestDto
     */
    private Long updateCustomerId(HttpServletRequest httpServletRequest, PartialTicketBookRequestDto partialTicketBookRequestDto) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        String userName = null;
        try {
            token = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
            User user = userInfoRepository.findByLoginId(userName);
            if (null == user) {
                log.info("User Not Found ::: " + userName);
                throw new UserNotFoundException("User not found in out system!!!");
            }
            return user.getId();
        } catch (Exception ex) {
            log.info("Token Broke" + ex.toString());
            throw new JwtExpiredException("We are facing issue in our end. Please try again sometime!");
        }
    }

    /**
     * @param partialTicketBookRequestDto
     */
    private void assertDateValidation(PartialTicketBookRequestDto partialTicketBookRequestDto) {
        if (!DateUtil.isTicketDateValid(partialTicketBookRequestDto.getMovieDate())) {
            log.info("Ticket date validation is failed! for: " + partialTicketBookRequestDto.getMovieDate());
            throw new BookingServiceException("Ticket date validation is failed. Please choose " +
                    "active date. User can book next 1 week tickets too!");
        }
        String[] count = partialTicketBookRequestDto.getTickets().split(",");
        if (!(count.length == partialTicketBookRequestDto.getNoOfPersons())) {
            log.info("No of person count and Ticket seats are not equal!" + partialTicketBookRequestDto.getTickets()
                    + ": No of person: " + partialTicketBookRequestDto.getNoOfPersons());
            throw new BookingServiceException("No of person count and Ticket seats are not equal!" +
                    "No of Person count: " + partialTicketBookRequestDto.getNoOfPersons() +
                    " Ticket count: " + count.length);
        }
    }

    /**
     * @param partialTicketBookRequestDto
     */
    private void sortSeatsInAscOrder(PartialTicketBookRequestDto partialTicketBookRequestDto) {
        String seats[] = partialTicketBookRequestDto.getTickets().split(",");
        Integer[] arr = new Integer[seats.length];
        for (int i = 0; i < seats.length; i++) {
            arr[i] = Integer.parseInt(seats[i]);
        }
        Collections.sort(Arrays.asList(arr));
        String.join(",", Arrays.toString(arr));
        partialTicketBookRequestDto.setTickets(Arrays.toString(arr).replace("]", "").replace("[", "").replace(" ", ""));
    }

    /**
     * @param partialTicketBookRequestDto
     */
    private void assertCheckTicketBookedPartially(PartialTicketBookRequestDto partialTicketBookRequestDto) {
        for (int i = 0; i < partialTicketBookRequestDto.getNoOfPersons(); i++) {
            PartialTicketBook partialTicketBook;
            // Find if this ticket already booked or not
            partialTicketBook = partialTicketBookRepository.findPartialTicketBooksSeats(
                    partialTicketBookRequestDto.getMovieId(), partialTicketBookRequestDto.getScreenId(),
                    partialTicketBookRequestDto.getTickets().split(",")[i],
                    partialTicketBookRequestDto.getShowId(), partialTicketBookRequestDto.getMovieDate(),
                    Boolean.TRUE);
            if (null != partialTicketBook && partialTicketBook.getIsPaymentDone()) {
                log.info("Ticket already booked : " + partialTicketBook.toString());
                throw new TicketNotAvailableException("This Ticket " + partialTicketBook.getSeatId() + " is already booked");
            }

            // Find if the ticket is partially booked and check the ticket sessions
            partialTicketBook = partialTicketBookRepository.findPartialTicketBooksSeats(
                    partialTicketBookRequestDto.getMovieId(), partialTicketBookRequestDto.getScreenId(),
                    partialTicketBookRequestDto.getTickets().split(",")[i],
                    partialTicketBookRequestDto.getShowId(), partialTicketBookRequestDto.getMovieDate(),
                    Boolean.FALSE);
            if (null != partialTicketBook) {
                //Check If the entry is exceeds 2 minutes user can allow to book these seats.
                assertCheckTicketExpiryInMinutes(partialTicketBook);
            }
        }
    }

    /**
     * @param partialTicketBook
     */
    public void assertCheckTicketExpiryInMinutes(PartialTicketBook partialTicketBook) {
        int minutes = DateUtil.findMinutes(partialTicketBook.getCreatedDate());
        log.info("##Minutes: " + minutes);
        if (ticketSession > minutes) {
            throw new TicketNotAvailableException("This Ticket " + partialTicketBook.getSeatId() + " is already booked");
        } else {
            partialTicketBook.setIsActive(Boolean.TRUE);
            // Updating isActive to TRUE i.e Soft delete. User can book this ticket.
            partialTicketBookRepository.save(partialTicketBook);
        }
    }

}
