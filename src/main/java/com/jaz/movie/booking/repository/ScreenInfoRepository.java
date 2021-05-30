package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScreenInfoRepository extends JpaRepository<Screen, Long> {

    @Query(value = "SELECT * FROM screen s WHERE s.FK_movie_id = :id",
            nativeQuery = true)
    List<Screen> findByMovieId(@Param("id") Long id);

    @Query(value = "SELECT * FROM screen s WHERE s.id = :screenId AND s.FK_show_id = :showId AND " +
            "s.FK_movie_id = :movieId", nativeQuery = true)
    Screen assertCheckMovieMappedInOurSystem(@Param("screenId") Long screenId, @Param("showId") Long showId,
                                             @Param("movieId") Long movieId);
}
