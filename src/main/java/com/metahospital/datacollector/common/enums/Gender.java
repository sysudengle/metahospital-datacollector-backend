package com.metahospital.datacollector.common.enums;

public enum Gender {
    Male(0),
    Female(1);

    private final int value;
    Gender(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static Gender convert(int value){
        switch (value) {
            case 0:
                return Male;
            case 1:
                return Female;
            default:
                return null;
        }
    }
}
