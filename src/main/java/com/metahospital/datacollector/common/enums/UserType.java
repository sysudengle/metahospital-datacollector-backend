package com.metahospital.datacollector.common.enums;

public enum UserType {
	Unknown(0),
	Patient(1),
    Doctor(2)
    ;

	private int type;
	
	private UserType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public static UserType valueOf(int type) {
        for (UserType userType : values()) {
            if (userType.getType() == type) {
                return userType;
            }
        }
        return Unknown;
    }
}
