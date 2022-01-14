package com.metahospital.datacollector.common.enums;

public enum BookingStatus {
    Processing(0),
    Completed(100);

    private int value;

    BookingStatus(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static BookingStatus convert(int value){
        switch (value) {
            case 0:
                return Processing;
            case 100:
                return Completed;
            default:
                return null;
        }
    }
}
