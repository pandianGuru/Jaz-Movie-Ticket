package com.jaz.movie.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScreenDto {

    private Long id;
    private int screenId;
    private List<MovieDto> moviesList;
    private String screenName;
    private int maxSeat;
    private BigDecimal amount;
    private String seatType;
    private List<MovieAvailableShowsDto> availableShows;
}
