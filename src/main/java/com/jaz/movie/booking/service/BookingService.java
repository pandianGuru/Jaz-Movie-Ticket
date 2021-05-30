package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.PartialTicketBookRequestDto;
import com.jaz.movie.booking.dto.PartialTicketBookResponseDto;
import com.jaz.movie.booking.entity.PartialTicketBook;

import javax.servlet.http.HttpServletRequest;

public interface BookingService {

    PartialTicketBookResponseDto bookPartialTicket(HttpServletRequest httpServletRequest, PartialTicketBookRequestDto partialTicketBookRequestDto);

    void assertCheckTicketExpiryInMinutes(PartialTicketBook partialTicketBook);
}
