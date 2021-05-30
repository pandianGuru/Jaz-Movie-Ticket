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

import javax.validation.Valid;

@RestController
@RequestMapping("/book")
@Slf4j
@Validated
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("/partial")
    public PartialTicketBookResponseDto partiallyBookingTicket(@Valid @RequestBody PartialTicketBookRequestDto partialTicketBookRequestDto) {
        log.info("Booking Movie: " + partialTicketBookRequestDto.toString());
        return bookingService.bookPartialTicket(partialTicketBookRequestDto);
    }


}
