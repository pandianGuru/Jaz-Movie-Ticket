package com.jaz.movie.booking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booked_ticket")
public class BookedTicket {
    @Id
    @GeneratedValue
    private Long bookedId;
    private Long movieId;
    private Long screenId;
    private Long showId;

    private Long paymentId;
    private BigDecimal amount;
    private String movieName;
    private String emailId;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.sql.Date movieBookedDate;
    private Date createdDate;
    private int noOfPersons;
    private String bookedSeats;
    private String paymentStatus;
    private String movieTime;
}
