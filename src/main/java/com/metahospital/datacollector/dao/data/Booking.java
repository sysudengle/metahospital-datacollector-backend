/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.dao.data;

import com.metahospital.datacollector.common.enums.BookingStatus;
import org.joda.time.DateTime;

import java.util.Date;

public class Booking {
	private int hospitalId;
	private long profileId;
	private long bookingId;
	private Date dateTime;
	private String comboIds;
	private int bookingStatus;

	public Booking() {
	}

	public Booking(int hospitalId, long profileId, long bookingId, Date dateTime, String comboIds, int bookingStatus) {
		this.hospitalId = hospitalId;
		this.profileId = profileId;
		this.bookingId = bookingId;
		this.dateTime = dateTime;
		this.comboIds = comboIds;
		this.bookingStatus = bookingStatus;
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

	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getComboIds() {
		return comboIds;
	}

	public void setComboIds(String comboIds) {
		this.comboIds = comboIds;
	}

	public int getBookingStatus() {return bookingStatus;}

	public void setBookingStatus(int bookingStatus) {this.bookingStatus = bookingStatus;}
}