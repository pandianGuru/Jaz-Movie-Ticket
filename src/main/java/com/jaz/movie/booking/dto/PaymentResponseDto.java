package com.jaz.movie.booking.dto;

import com.jaz.movie.booking.entity.BookedTicket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentResponseDto {

    private BookedTicket bookedTicket;
}
