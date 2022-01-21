package com.metahospital.datacollector.common.enums;

import org.omg.CORBA.DynAnyPackage.Invalid;

import javax.validation.Valid;

public enum Gender {
    Unknow(0),
    Male(1),
    Female(2);

    private final int value;
    Gender(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static Gender convert(int value) {
        Gender gender = Unknow;
        if (value == Male.getValue()) {
            gender = Male;
        } else if (value == Female.getValue()) {
            gender = Female;
        }

        return gender;
    }
}
