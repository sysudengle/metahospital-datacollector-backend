package com.metahospital.datacollector.common.enums;

public enum DoctorStatus {
    Unknown(0),
    UnderApply(1),
    Valid(2),
    Invalid(3)
    ;
    
    private int status;
	
	private DoctorStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static DoctorStatus valueOf(int status) {
        for (DoctorStatus doctorStatus : values()) {
            if (doctorStatus.getStatus() == status) {
                return doctorStatus;
            }
        }
        return Unknown;
    }

}
