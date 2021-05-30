package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.MovieAvailableShowsData;
import com.jaz.movie.booking.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvailableShowsInfoRepository extends JpaRepository<MovieAvailableShowsData, Long> {

}
