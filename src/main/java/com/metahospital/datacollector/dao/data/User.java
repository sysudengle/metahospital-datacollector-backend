/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.dao.data;

import com.metahospital.datacollector.common.enums.UserType;

public class User {
	private long userId;
	private String name;
	private UserType userType;

	public User() {
	}

	public User(long userId, String name, UserType userType) {
		this.userId = userId;
		this.name = name;
		this.userType = userType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
}