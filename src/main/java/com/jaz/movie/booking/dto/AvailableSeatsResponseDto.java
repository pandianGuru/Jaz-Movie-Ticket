package com.jaz.movie.booking.dto;

import com.jaz.movie.booking.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AvailableSeatsResponseDto {

    private Movie movie;
    private Map<Integer, String> seats;
    private Map<String, BigDecimal> seatInfo;
}
