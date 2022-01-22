package com.metahospital.datacollector.common.enums;

public enum BookingStatus {
    Unknown(0),
    Processing(1),
    Completed(2);

    private int value;

    BookingStatus(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static BookingStatus valueOf(int value) {
        for (BookingStatus bookingStatus : values()) {
            if (bookingStatus.getValue() == value) {
                return bookingStatus;
            }
        }
        return Unknown;
    }
}
