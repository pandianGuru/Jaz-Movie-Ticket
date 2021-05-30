package com.jaz.movie.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartialTicketBookResponseDto {

    private Long id;
    private Long screenId;
    private Long movieId;
    private Long showId;
    private Long userId;
    private Integer noOfPersons;
    private String seats;
    private String movieTime;
    private Boolean isPaymentDone;
    private java.sql.Date movieDate;
}
