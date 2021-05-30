package com.jaz.movie.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentRequestDto {

    private Long partialBookId;
    private Long movieId;
    private Long screenId;
    private Long userId;
    private Long showId;
    private BigDecimal amount;
    private String movieName;
    private String tickets;
    private int noOfPersons;
    private String paymentType;
    private String movieTime;
    private String userName;
    private String emailId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.sql.Date movieDate;
}
