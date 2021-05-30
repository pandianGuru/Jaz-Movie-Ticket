package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.AvailableSeatsRequestDto;
import com.jaz.movie.booking.dto.AvailableSeatsResponseDto;
import com.jaz.movie.booking.dto.MovieScreenResponseDto;
import com.jaz.movie.booking.entity.Movie;

import java.util.List;

public interface MovieInfoService {

    List<Movie> getMovieList();

    Movie save(Movie movie);

    MovieScreenResponseDto getMovie(Long id);

    AvailableSeatsResponseDto getAvailableSeats(AvailableSeatsRequestDto availableSeatsRequestDto);
}
