package com.terry.librarysearch;

public class RoomTimeItem {
    private String timeRange;
    private String status;
    private String reservedPerson;

    public RoomTimeItem(String timeRange, String status, String reservedPerson) {
        this.timeRange = timeRange;
        this.status = status;
        this.reservedPerson = reservedPerson;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public String getStatus() {
        return status;
    }

    public String getReservedPerson() {
        return reservedPerson;
    }
}
