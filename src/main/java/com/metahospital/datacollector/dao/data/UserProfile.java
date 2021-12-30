/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.dao.data;

import com.metahospital.datacollector.common.enums.UserType;

public class UserProfile {
	private long userId;
	private int hospitalId;
	private long profileId;

	public UserProfile() {
	}

	public UserProfile(long userId, int hospitalId, long profileId) {
		this.userId = userId;
		this.hospitalId = hospitalId;
		this.profileId = profileId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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
}