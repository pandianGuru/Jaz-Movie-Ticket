package com.jaz.movie.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartialTicketBookRequestDto {

    //@Size(max=6,min=1,message="User can choose 6 seats maximum and 1 seats minimum. Validation failed please select again.")
    @Min(value = 1, message = "User can select the seat minimum 1. Please choose minimum 1 and maximum 6.")
    @Max(value = 6, message = "User can select the seats maximum 6. Please choose lesser than 6.")
    private int noOfPersons;
    @NotBlank(message = "Seat ids are missing.")
    private String tickets;
    @Positive(message = "Movie Id should be greater than zero.")
    private Long movieId;
    @Positive(message = "Screen Id should be greater than zero.")
    private Long screenId;
    @Positive(message = "Show Id should be greater than zero.")
    private Long showId;
    @NotEmpty(message = "Movie Time field is missing.")
    private String movieTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.sql.Date movieDate;
}
