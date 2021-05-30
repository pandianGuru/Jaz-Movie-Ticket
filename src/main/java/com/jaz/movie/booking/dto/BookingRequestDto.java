package com.jaz.movie.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingRequestDto {
    private Long movieId;
    private Long screenId;
    private String showTime;
    private String screenName;
    private Integer[] allocatedSeats;

}
