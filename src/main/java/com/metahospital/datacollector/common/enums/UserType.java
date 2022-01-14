package com.metahospital.datacollector.common.enums;

public enum UserType {
	Unknown(0),
	Patient(5),
    Doctor(10);

    private final int value;
    UserType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static UserType convert(int value){
        switch (value) {
            case 0:
                return Unknown;
            case 5:
                return Patient;
            case 10:
                return Doctor;
            default:
                return null;
        }
    }
}
