package com.jaz.movie.booking.dto;

import com.jaz.movie.booking.entity.Movie;
import com.jaz.movie.booking.entity.SeatInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AvailableSeatsResponseDto {

    private Movie movie;
    private Map<Integer, String> seats;
    private List<SeatInfo> seatInfo;
}
