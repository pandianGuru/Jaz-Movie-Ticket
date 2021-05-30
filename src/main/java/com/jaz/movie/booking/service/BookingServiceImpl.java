package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.PartialTicketBookRequestDto;
import com.jaz.movie.booking.dto.PartialTicketBookResponseDto;
import com.jaz.movie.booking.entity.PartialTicketBook;
import com.jaz.movie.booking.exception.BookingServiceException;
import com.jaz.movie.booking.exception.TicketNotAvailableException;
import com.jaz.movie.booking.repository.PartialTicketBookRepository;
import com.jaz.movie.booking.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    PartialTicketBookRepository partialTicketBookRepository;

    @Override
    public PartialTicketBookResponseDto bookPartialTicket(PartialTicketBookRequestDto partialTicketBookRequestDto) {
        //General Validation
        assertDateValidation(partialTicketBookRequestDto);

        //check ticket is already partially booked or not
        assertCheckTicketBookedPartially(partialTicketBookRequestDto);

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
            partialTicketBook.setUserId(111L);
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
        Collections.sort(Arrays.asList(seats));
        partialTicketBookRequestDto.setTickets(String.join(",", seats));
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
        if (2 > minutes) {
            throw new TicketNotAvailableException("This Ticket " + partialTicketBook.getSeatId() + " is already booked");
        } else {
            partialTicketBook.setIsActive(Boolean.TRUE);
            // Doing soft delete. User can book this ticket.
            partialTicketBookRepository.save(partialTicketBook);
        }
    }

}
