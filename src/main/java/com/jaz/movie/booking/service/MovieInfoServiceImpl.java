package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.AvailableSeatsRequestDto;
import com.jaz.movie.booking.dto.AvailableSeatsResponseDto;
import com.jaz.movie.booking.dto.MovieScreenResponseDto;
import com.jaz.movie.booking.entity.Movie;
import com.jaz.movie.booking.entity.Screen;
import com.jaz.movie.booking.entity.SeatInfo;
import com.jaz.movie.booking.exception.BookingServiceException;
import com.jaz.movie.booking.exception.MovieNotFoundException;
import com.jaz.movie.booking.repository.MovieInfoRepository;
import com.jaz.movie.booking.repository.PartialTicketBookRepository;
import com.jaz.movie.booking.repository.ScreenInfoRepository;
import com.jaz.movie.booking.repository.SeatInfoInfoRepository;
import com.jaz.movie.booking.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class MovieInfoServiceImpl implements MovieInfoService {

    @Autowired
    ScreenInfoRepository screenInfoRepository;

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    SeatInfoInfoRepository seatInfoInfoRepository;

    @Autowired
    PartialTicketBookRepository partialTicketBookRepository;

    @Override
    public List<Movie> getMovieList() {
        return movieInfoRepository.findAll();
    }

    @Override
    public Movie save(Movie movie) {
        return movieInfoRepository.save(movie);
    }

    @Override
    public MovieScreenResponseDto getMovie(Long id) {
        Optional<Movie> movieOptional = movieInfoRepository.findById(id);
        if (!movieOptional.isPresent()) {
            log.info("Invalid movie id:: " + id);
            throw new MovieNotFoundException("Invalid movie id " + id);
        }
        Movie movie = movieOptional.get();
        List<Screen> screens = screenInfoRepository.findByMovieId(movie.getId());
        MovieScreenResponseDto movieScreenResponseDto = new MovieScreenResponseDto();
        movieScreenResponseDto.setMovie(movie);
        movieScreenResponseDto.setScreens(screens);
        return movieScreenResponseDto;
    }

    @Override
    public AvailableSeatsResponseDto getAvailableSeats(AvailableSeatsRequestDto availableSeatsRequestDto) {
        log.info("Method: getAvailableSeats : " + availableSeatsRequestDto.toString());
        AvailableSeatsResponseDto availableSeats = new AvailableSeatsResponseDto();
        if (!DateUtil.isTicketDateValid(availableSeatsRequestDto.getMovieDate())) {
            log.info("Ticket date validation is failed! for: " + availableSeatsRequestDto.getMovieDate());
            throw new BookingServiceException("Ticket date validation is failed. Please choose " +
                    "active date. User can book next 1 week tickets too!");
        }
        //Cross Check the movie id, show id, screen id is exist in our system.
        Screen availableData = screenInfoRepository.assertCheckMovieMappedInOurSystem(availableSeatsRequestDto.getScreenId(), availableSeatsRequestDto.getShowId(),
                availableSeatsRequestDto.getMovieId());
        if (null == availableData) {
            log.info("There is no movie exist in this screen: " + availableSeatsRequestDto.toString());
            throw new BookingServiceException("There is no movie exist in this screen.");
        }

        List<String> seatIds = partialTicketBookRepository.findBookedSeats(
                availableSeatsRequestDto.getMovieId(), availableSeatsRequestDto.getScreenId(),
                availableSeatsRequestDto.getShowId(), availableSeatsRequestDto.getMovieDate(),
                Boolean.TRUE);

        //Find the screen maximum seat capacity
        List<Screen> screens = screenInfoRepository.findByMovieId(availableSeatsRequestDto.getMovieId());
        List<SeatInfo> seatTypes = seatInfoInfoRepository.findAll();
        availableSeats.setSeatInfo(seatTypes);
        Map<Integer, String> map = getAvailableSeats(seatIds, screens.stream().findFirst().get().getMaxSeat());
        availableSeats.setSeats(map);

        Optional<Movie> movieOptional = movieInfoRepository.findById(availableSeatsRequestDto.getMovieId());
        Movie movie = movieOptional.get();
        availableSeats.setMovie(movie);

        return availableSeats;
    }

    /**
     * @param bookedSeats
     * @param seatCapacity
     * @return
     */
    private Map<Integer, String> getAvailableSeats(List<String> bookedSeats, int seatCapacity) {
        Map<Integer, String> map = new HashMap<>();
        if (!bookedSeats.isEmpty()) {
            for (int i = 0; i < bookedSeats.size(); i++) {
                map.put(Integer.valueOf(bookedSeats.get(i)), "N/A");
            }
        }
        for (int i = 1; i <= seatCapacity; i++) {
            if (null == map.get(i)) {
                map.put(i, "AV");
            }
        }
        log.info("Available Seats!! ", map.toString());
        return map;
    }

}
