package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.PartialTicketBookRequestDto;
import com.jaz.movie.booking.dto.PartialTicketBookResponseDto;
import com.jaz.movie.booking.entity.PartialTicketBook;

import javax.servlet.http.HttpServletRequest;

public interface BookingService {

    /**
     * <p>
     * First do the General validation like date validation, movie, screen mapping validation.
     * Check the ticket is already booked or not validation.
     * If the ticket is booked my some other user and the ticket session is within 'ticketSession' minutes it will throw and exception.
     * If the ticket is booked my some other user and the ticket session is exceeds 'ticketSession' minutes it will allow to book.
     *
     * </p>
     *
     * @param httpServletRequest
     * @param partialTicketBookRequestDto
     * @return
     */
    PartialTicketBookResponseDto bookPartialTicket(HttpServletRequest httpServletRequest, PartialTicketBookRequestDto partialTicketBookRequestDto);


    /**
     * <p>
     * If the ticket is booked my some other user and the ticket session is within 'ticketSession' minutes it will throw and exception.
     * If the ticket is booked my some other user and the ticket session is exceeds 'ticketSession' minutes it will allow to book.
     *
     * </p>
     *
     * @param partialTicketBook
     * @return
     */
    void assertCheckTicketExpiryInMinutes(PartialTicketBook partialTicketBook);
}
