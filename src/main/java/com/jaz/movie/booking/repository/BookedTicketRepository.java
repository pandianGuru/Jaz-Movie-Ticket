package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.BookedTicket;
import com.jaz.movie.booking.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BookedTicketRepository extends JpaRepository<BookedTicket, Long> {

    @Query(value = "SELECT booked_seats FROM booked_ticket t WHERE t.movie_id = :movieId " +
            "AND t.screen_id = :screenId AND t.show_id = :showId AND movie_booked_date = :date",
            nativeQuery = true)
    List<String> findBookedSeats(@Param("movieId") Long movieId, @Param("screenId") Long screenId,
                                       @Param("showId") Long show_id, @Param("date") Date date);
}
