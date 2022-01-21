package com.metahospital.datacollector.common.enums;

public enum DoctorStatus {
    Unknown(0),
    UnderApply(1),
    Valid(2),
    Invalid(3);

    private final int value;
    DoctorStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static DoctorStatus convert(int value) {
        DoctorStatus status = Unknown;
        if (value == UnderApply.getValue()) {
            status = UnderApply;
        } else if (value == Valid.getValue()) {
            status = Valid;
        } else if (value == Invalid.getValue()) {
            status = Invalid;
        }

        return status;
    }

}
