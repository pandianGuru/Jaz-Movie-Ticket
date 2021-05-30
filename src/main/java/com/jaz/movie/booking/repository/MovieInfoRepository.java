package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieInfoRepository extends JpaRepository<Movie,Long> {
}
