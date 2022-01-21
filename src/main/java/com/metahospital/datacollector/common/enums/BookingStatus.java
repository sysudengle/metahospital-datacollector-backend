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

    public static BookingStatus convert(int value) {
        BookingStatus status = Unknown;
        if (value == Processing.getValue()) {
            status = Processing;
        } else if (value == Completed.getValue()) {
            status = Completed;
        }

        return status;
    }
}
