package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.PartialTicketBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


public interface PartialTicketBookRepository extends JpaRepository<PartialTicketBook, Long> {

    @Query(value = "SELECT * FROM partial_ticket_booking t WHERE t.movie_id = :movieId " +
            "AND t.screen_id = :screenId AND t.seat_id = :seatId AND t.show_id = :showId AND t.movie_date = :date " +
            "AND t.is_active = :check",
            nativeQuery = true)
    PartialTicketBook findPartialTicketBooksSeats(@Param("movieId") Long movieId, @Param("screenId") Long screenId,
                                                  @Param("seatId") String seatId, @Param("showId") Long show_id,
                                                  @Param("date") Date date, @Param("check") Boolean check);

    @Query(value = "SELECT * FROM partial_ticket_booking t WHERE t.id = :partialBookId " +
            "AND t.is_payment_done = :check",
            nativeQuery = true)
    PartialTicketBook checkPaymentDoneOrNot(@Param("partialBookId") Long partialBookId,
                                            @Param("check") Boolean check);


    @Query(value = "SELECT t2.id " +
            "    FROM  partial_ticket_booking AS t1, partial_ticket_booking AS t2 " +
            "    WHERE " +
            "    t1.movie_id = t2.movie_id AND " +
            "    t1.screen_id = t2.screen_id AND " +
            "    t1.show_id =  t2.show_id  AND " +
            "    t1.movie_date = t2.movie_date AND " +
            "    t1.seats = t2.seats AND " +
            "    t1.is_active = 0 AND " +
            "    t1.id = :id",
            nativeQuery = true)
    List<Integer> fetchPartialTicketBooksSeatsById(@Param("id") Long id);

    @Query(value = "SELECT t.seat_id FROM partial_ticket_booking t WHERE t.movie_id = :movieId " +
            "AND t.screen_id = :screenId AND t.show_id = :showId AND t.movie_date = :date " +
            "AND t.is_payment_done = :check OR created_date > NOW() - INTERVAL 2 MINUTE",
            nativeQuery = true)
    List<String> findBookedSeats(@Param("movieId") Long movieId, @Param("screenId") Long screenId,
                                 @Param("showId") Long show_id, @Param("date") Date date,
                                 @Param("check") Boolean check);

    @Query
    Optional<PartialTicketBook> findByIdAndIsActiveFalse(@Param("id") Long id);

}
