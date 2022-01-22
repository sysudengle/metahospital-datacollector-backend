package com.metahospital.datacollector.common.enums;

public enum UserType {
	Unknown(0),
	Patient(1),
    Doctor(2);

    private final int value;
    
    UserType(int value){
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }

    public static UserType valueOf(int value){
        for (UserType userType : values()) {
            if (userType.getValue() == value) {
                return userType;
            }
        }
        return Unknown;
    }
}
