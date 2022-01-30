package com.metahospital.datacollector.controller.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetWXProfilesRspDto extends BaseDto {
    private List<ProfileInfoDto> profiles;

    public GetWXProfilesRspDto() {
    }

    public GetWXProfilesRspDto(List<ProfileInfoDto> profiles) {
        this.profiles = profiles;
    }

    public List<ProfileInfoDto> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileInfoDto> profiles) {
        this.profiles = profiles;
    }
}
