package com.metahospital.datacollector.common.enums;

public enum ItemType {
	Unknown(0),
    NumberType(1),
    StringType(2),
    SelectionType(3),
    ;

    private final int value;
    
    ItemType(int value){
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }

    public static ItemType valueOf(int value){
        for (ItemType itemType : values()) {
            if (itemType.getValue() == value) {
                return itemType;
            }
        }
        return Unknown;
    }

}
