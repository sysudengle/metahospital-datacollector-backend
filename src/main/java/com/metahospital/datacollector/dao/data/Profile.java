/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.dao.data;

import com.metahospital.datacollector.common.enums.Gender;

public class Profile {
	private int hospitalId;
	private long profileId;
	private String personalID;
	private int gender;
	private String pidAddress;
	private String homeAddress;

	public Profile() {
	}

	public Profile(int hospitalId, long profileId, String personalID, int gender, String pidAddress, String homeAddress) {
		this.hospitalId = hospitalId;
		this.profileId = profileId;
		this.personalID = personalID;
		this.gender = gender;
		this.pidAddress = pidAddress;
		this.homeAddress = homeAddress;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public String getPersonalID() {
		return personalID;
	}

	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getPidAddress() {
		return pidAddress;
	}

	public void setPidAddress(String pidAddress) {
		this.pidAddress = pidAddress;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
}