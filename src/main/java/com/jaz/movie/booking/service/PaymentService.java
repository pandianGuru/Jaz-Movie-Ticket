package com.jaz.movie.booking.service;

import com.jaz.movie.booking.dto.PaymentRequestDto;
import com.jaz.movie.booking.entity.BookedTicket;

import java.util.TreeMap;

public interface PaymentService {

    BookedTicket paymentValidationAndUpdatePayment(PaymentRequestDto paymentRequestDto, TreeMap<String, String> parameters);

    boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception;

    String getCheckSum (TreeMap < String, String > parameters) throws Exception;

    BookedTicket updatePayment(TreeMap<String, String> parameters, Long bookingId);

    void assertCheckTicketSession(Long bookingId);
}
