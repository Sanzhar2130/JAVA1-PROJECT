package com.example.jp_1.model;

public class Genre {
    private Integer gid;
    private String name;

    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public Genre(Integer gid, String name) {
        this.gid = gid;
        this.name = name;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


