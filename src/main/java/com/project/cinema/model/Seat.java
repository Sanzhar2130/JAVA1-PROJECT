package com.project.cinema.model;

public class Seat {
    private Integer sid;
    private Integer hid;
    private Integer rowNumber;
    private Integer seatNumber;
    private Boolean isVip;

    public Seat() {}

    public Seat(Integer hid, Integer rowNumber, Integer seatNumber) {
        this.hid = hid;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    public Seat(Integer sid, Integer hid, Integer rowNumber, Integer seatNumber, Boolean isVip) {
        this.sid = sid;
        this.hid = hid;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isVip = isVip;
    }

    public Integer getSid() {
        return sid;
    }
    public void setSid(Integer sid) {
        this.sid = sid;
    }
    public Integer getHid() {
        return hid;
    }
    public void setHid(Integer hid) {
        this.hid = hid;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }
    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }
    public Integer getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Boolean getIsVip() {
        return isVip;
    }
    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }
}
