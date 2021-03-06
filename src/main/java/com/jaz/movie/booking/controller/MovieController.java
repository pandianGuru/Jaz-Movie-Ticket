package com.jaz.movie.booking.controller;

import com.jaz.movie.booking.dto.AvailableSeatsRequestDto;
import com.jaz.movie.booking.dto.AvailableSeatsResponseDto;
import com.jaz.movie.booking.dto.MovieScreenResponseDto;
import com.jaz.movie.booking.entity.Movie;
import com.jaz.movie.booking.service.MovieInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * Create move, Get Movies and get the seat available detail
 * </p>
 *
 * @author pandian.gurunathan
 */
@RestController
@RequestMapping("/movie")
@Slf4j
public class MovieController {

    @Autowired
    MovieInfoService movieInfoService;

    /**
     * <p>
     * Create movie in our system
     * </p>
     *
     * @param movie
     * @return
     */
    @PostMapping("/create")
    public Movie createMovie(@RequestBody Movie movie) {
        log.info("Create Movie: " + movie.toString());
        return movieInfoService.save(movie);
    }

    @GetMapping("/list")
    public List<Movie> getAllMovieList() {
        return movieInfoService.getMovieList();
    }

    @GetMapping("/{movieId}")
    public MovieScreenResponseDto getMovie(@PathVariable("movieId") Long movieId) {
        return movieInfoService.getMovie(movieId);
    }

    /**
     * Fetch the free seats by movie id, screen id, show id, date
     *
     * @param availableSeatsRequestDto
     * @return
     */
    @PutMapping("/get-free-seats")
    public AvailableSeatsResponseDto getAvailableSeats(@RequestBody AvailableSeatsRequestDto availableSeatsRequestDto) {
        log.info("Method: getAvailableSeats : " + availableSeatsRequestDto.toString());
        return movieInfoService.getAvailableSeats(availableSeatsRequestDto);
    }
}
