package com.jaz.movie.booking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "partial_ticket_booking")
public class PartialTicketBook {

    @Id
    @GeneratedValue
    private Long id;
    private Long screenId;
    private Long movieId;
    private Long showId;
    private Long userId;
    private Long bookedTicketId;
    private Integer noOfPersons;
    private String seats;
    private String seatId;
    private Boolean isPaymentDone;
    private Boolean isActive;
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.sql.Date movieDate;
}
