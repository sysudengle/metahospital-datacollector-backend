package com.metahospital.datacollector.common.enums;

public enum DoctorStatus {
    Unknown(0),
    UnderApply(50),
    Valid(200),
    Invalid(400);

    private final int value;
    DoctorStatus(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static DoctorStatus convert(int value){
        switch (value) {
            case 0:
                return Unknown;
            case 50:
                return UnderApply;
            case 200:
                return Valid;
            case 400:
                return Invalid;
            default:
                return null;
        }
    }

}
