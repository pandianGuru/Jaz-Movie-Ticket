package com.jaz.movie.booking.exception;

public class TicketNotAvailableException extends RuntimeException {

    public TicketNotAvailableException(String msg) {
        super(msg);
    }
}
