package com.jaz.movie.booking.dto;

import com.jaz.movie.booking.entity.Movie;
import com.jaz.movie.booking.entity.Screen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieScreenResponseDto {
    private Movie movie;
    private List<Screen> screens;
}
