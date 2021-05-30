package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
