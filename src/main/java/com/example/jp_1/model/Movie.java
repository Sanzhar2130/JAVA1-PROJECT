package com.example.jp_1.model;

public class Movie {
    private Integer mid;
    private Integer gid;
    private String title;
    private String director;
    private Integer durationMinutes;
    private String description;

    public Movie() {
    }

    public Movie(Integer gid, String title, String director, Integer durationMinutes, String description) {
        this.gid = gid;
        this.title = title;
        this.director = director;
        this.durationMinutes = durationMinutes;
        this.description = description;
    }

    public Movie(Integer mid, Integer gid, String title, String director, Integer durationMinutes, String description) {
        this.mid = mid;
        this.gid = gid;
        this.title = title;
        this.director = director;
        this.durationMinutes = durationMinutes;
        this.description = description;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title;
    }

}
