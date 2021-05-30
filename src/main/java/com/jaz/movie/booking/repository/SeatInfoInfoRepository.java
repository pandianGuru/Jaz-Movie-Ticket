package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.SeatInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatInfoInfoRepository extends JpaRepository<SeatInfo, Long> {
}
