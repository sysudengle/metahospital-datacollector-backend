package com.metahospital.datacollector.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;

/**
 * Created on 2021/12/11.
 */
/**
 * Updated on 2021/12/12.
 */
@Data

public class WechatAccount {

	private String openId;

	private String unionId;

	private String sessionKey;

	private long userId;

	public WechatAccount() {
	}
	
	public WechatAccount(String openId, String unionId, String sessionKey, long userId) {
		this.openId = openId;
		this.unionId = unionId;
		this.sessionKey = sessionKey;
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
