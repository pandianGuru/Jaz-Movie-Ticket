package com.jaz.movie.booking.dto;

public enum PaymentStatus {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    private String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
