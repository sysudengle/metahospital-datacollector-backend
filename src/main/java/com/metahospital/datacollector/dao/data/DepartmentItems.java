package com.metahospital.datacollector.dao.data;

public class DepartmentItems {
    private int hospitalId;
    private long profileId;
    private long bookingId;
    private long departmentId;
    private String itemValues;

    public DepartmentItems() {
    }

    public DepartmentItems(int hospitalId, long profileId, long bookingId, long departmentId, String itemValues) {
        this.hospitalId = hospitalId;
        this.profileId = profileId;
        this.bookingId = bookingId;
        this.departmentId = departmentId;
        this.itemValues = itemValues;
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

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public String getItemValues() {
        return itemValues;
    }

    public void setItemValues(String itemValues) {
        this.itemValues = itemValues;
    }
}
