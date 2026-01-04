package com.project.cinema.model;

import java.time.LocalDateTime;

public class Session {
    private Integer sessId;
    private Integer mid;
    private Integer hid;
    private LocalDateTime startTime;
    private Double basePrice;

    public Session() {
    }

    public Session(Integer mid, Integer hid, LocalDateTime startTime, Double basePrice) {
        this.mid = mid;
        this.hid = hid;
        this.startTime = startTime;
        this.basePrice = basePrice;
    }

    public Session(Integer sessId, Integer mid, Integer hid, LocalDateTime startTime, Double basePrice) {
        this.sessId = sessId;
        this.mid = mid;
        this.hid = hid;
        this.startTime = startTime;
        this.basePrice = basePrice;
    }

    public Integer getSessId() {
        return sessId;
    }

    public void setSessId(Integer sessId) {
        this.sessId = sessId;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getHid() {
        return hid;
    }

    public void setHid(Integer hid) {
        this.hid = hid;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }
}
