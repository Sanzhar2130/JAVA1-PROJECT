package com.project.cinema.model;

public class Hall {
    private Integer hid;
    private String name;
    private String screenType;

    public Hall() {}

    public Hall(String name, String screenType) {
        this.name = name;
        this.screenType = screenType;
    }

    public Hall(Integer hid, String name, String screenType) {
        this.hid = hid;
        this.name = name;
        this.screenType = screenType;
    }

    public Integer getHid() {
        return hid;
    }

    public void setHid(Integer hid) {
        this.hid = hid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }
}
