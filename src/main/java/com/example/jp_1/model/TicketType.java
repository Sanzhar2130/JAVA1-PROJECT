package com.example.jp_1.model;

public class TicketType {
    private Integer ttid;
    private String name;
    private Double discountPercent;

    public TicketType() {
    }

    public TicketType(String name, Double discountPercent) {
        this.name = name;
        this.discountPercent = discountPercent;
    }

    public TicketType(Integer ttid, String name, Double discountPercent) {
        this.ttid = ttid;
        this.name = name;
        this.discountPercent = discountPercent;
    }

    public Integer getTtId() {
        return ttid;
    }

    public void setTtId(Integer ttid) {
        this.ttid = ttid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }
}
