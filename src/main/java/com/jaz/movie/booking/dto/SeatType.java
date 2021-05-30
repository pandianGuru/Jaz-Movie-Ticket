package com.jaz.movie.booking.dto;

public enum SeatType {
    NORMAL("NORMAL"),
    VIP("VIP");

    private String seatType;

    SeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getSeatType() {
        return seatType;
    }
}
