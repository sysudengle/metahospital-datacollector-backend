package com.metahospital.datacollector.common.enums;

public enum DoctorStatus {
    Unknown(0),
    UnderApply(1),
    Valid(2),
    Invalid(3);

    private final int value;
    
    DoctorStatus(int value){
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }

    public static DoctorStatus valueOf(int value){
        for (DoctorStatus doctorStatus : values()) {
            if (doctorStatus.getValue() == value) {
                return doctorStatus;
            }
        }
        return Unknown;
    }
}
