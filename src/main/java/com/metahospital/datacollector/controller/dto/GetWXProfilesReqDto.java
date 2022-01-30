package com.metahospital.datacollector.controller.dto;

public class GetWXProfilesReqDto extends BaseDto {
    private int hospitalId;

    public GetWXProfilesReqDto() {
    }

    public GetWXProfilesReqDto(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getHospitalId() {
        return hospitalId;
    }
}
