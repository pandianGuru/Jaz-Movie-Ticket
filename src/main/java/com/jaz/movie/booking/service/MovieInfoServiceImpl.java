package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.AvailableSeatsRequestDto;
import com.jaz.movie.booking.dto.AvailableSeatsResponseDto;
import com.jaz.movie.booking.dto.MovieScreenResponseDto;
import com.jaz.movie.booking.dto.SeatType;
import com.jaz.movie.booking.entity.Movie;
import com.jaz.movie.booking.entity.MovieAvailableShowsData;
import com.jaz.movie.booking.entity.Screen;
import com.jaz.movie.booking.exception.BookingServiceException;
import com.jaz.movie.booking.exception.MovieNotFoundException;
import com.jaz.movie.booking.repository.AvailableShowsInfoRepository;
import com.jaz.movie.booking.repository.MovieInfoRepository;
import com.jaz.movie.booking.repository.PartialTicketBookRepository;
import com.jaz.movie.booking.repository.ScreenInfoRepository;
import com.jaz.movie.booking.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class MovieInfoServiceImpl implements MovieInfoService {

    @Autowired
    AvailableShowsInfoRepository availableShowsInfoRepository;

    @Autowired
    ScreenInfoRepository screenInfoRepository;

    @Autowired
    MovieInfoRepository movieInfoRepository;

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
            log.info("Invalid movie it:: " + id);
            throw new MovieNotFoundException("Invalid movie id " + id);
        }
        Movie movie = movieOptional.get();
        List<Screen> screens = screenInfoRepository.findByMovieId(movie.getId());
        List<MovieAvailableShowsData> showsData = availableShowsInfoRepository.findAll();
        MovieScreenResponseDto movieScreenResponseDto = new MovieScreenResponseDto();
        movieScreenResponseDto.setMovie(movie);
        screens.stream().forEach(action -> {
            action.setAvailableShows(showsData);
        });
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
        List<String> seatIds = partialTicketBookRepository.findBookedSeats(
                availableSeatsRequestDto.getMovieId(), availableSeatsRequestDto.getScreenId(),
                availableSeatsRequestDto.getShowId(), availableSeatsRequestDto.getMovieDate(),
                Boolean.TRUE);
        String bookedSeats = "EMPTY";
        if (null != seatIds && !seatIds.isEmpty()) {
            bookedSeats = String.join(",", seatIds);
        }

        //Find the screen maximum seat capacity
        Map<String, BigDecimal> seatInfo = new HashMap<>();
        List<Screen> screens = screenInfoRepository.findByMovieId(availableSeatsRequestDto.getMovieId());
        screens.stream().forEach(action -> {
            if(action.getSeatType().equalsIgnoreCase(SeatType.NORMAL.toString())){
                seatInfo.put(SeatType.NORMAL.toString(), action.getAmount());
            }
            if(action.getSeatType().equalsIgnoreCase(SeatType.VIP.toString())){
                seatInfo.put(SeatType.VIP.toString(), action.getAmount());
            }
        });
        availableSeats.setSeatInfo(seatInfo);
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
