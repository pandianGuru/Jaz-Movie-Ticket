package com.jaz.movie.booking.controller;

import com.jaz.movie.booking.dto.PartialTicketBookRequestDto;
import com.jaz.movie.booking.dto.PartialTicketBookResponseDto;
import com.jaz.movie.booking.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/book")
@Slf4j
@Validated
public class BookingController {

    @Autowired
    BookingService bookingService;

    /**
     * <p>
     * User can book the ticket partially.
     * </p>
     *
     * @param httpServletRequest          Holds the server request object. Used to get Auth detials
     * @param partialTicketBookRequestDto Holds the ticket info to save in database
     * @return partial ticket booked response
     */
    @PostMapping("/partial")
    public PartialTicketBookResponseDto partiallyBookingTicket(HttpServletRequest httpServletRequest,
                                                               @Valid @RequestBody PartialTicketBookRequestDto partialTicketBookRequestDto) {
        log.info("Booking Movie: " + partialTicketBookRequestDto.toString());
        return bookingService.bookPartialTicket(httpServletRequest, partialTicketBookRequestDto);
    }
}
