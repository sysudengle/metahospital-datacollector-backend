package com.metahospital.datacollector.dao.data;

public class InnerAccount {
	private String accountName;
	private String password;
	private long userId;

	public InnerAccount() {
	}

	public InnerAccount(String accountName, String password, long userId) {
		this.accountName = accountName;
		this.password = password;
		this.userId = userId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
