package com.jaz.movie.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieAvailableShowsDto {
    private Long id;
    private String showTimings;
}
