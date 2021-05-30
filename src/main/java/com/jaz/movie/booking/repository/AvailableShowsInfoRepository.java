package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.MovieAvailableShows;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableShowsInfoRepository extends JpaRepository<MovieAvailableShows, Long> {

}
