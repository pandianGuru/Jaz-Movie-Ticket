package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.PartialTicketBookRequestDto;
import com.jaz.movie.booking.dto.PartialTicketBookResponseDto;
import com.jaz.movie.booking.entity.PartialTicketBook;

public interface BookingService {

    PartialTicketBookResponseDto bookPartialTicket(PartialTicketBookRequestDto partialTicketBookRequestDto);

    void assertCheckTicketExpiryInMinutes(PartialTicketBook partialTicketBook);
}
