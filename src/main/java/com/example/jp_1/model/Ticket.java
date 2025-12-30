package com.example.jp_1.model;

public class Ticket {
    private Integer tid;
    private Integer bid;
    private Integer sessId;
    private Integer sid;
    private Integer ttid;
    private Double price;

    public Ticket() {
    }

    public Ticket(Integer bid, Integer sessId, Integer sid, Integer ttid, Double price) {
        this.bid = bid;
        this.sessId = sessId;
        this.sid = sid;
        this.ttid = ttid;
        this.price = price;
    }

    public Ticket(Integer tid, Integer bid, Integer sessId, Integer sid, Integer ttid, Double price) {
        this.tid = tid;
        this.bid = bid;
        this.sessId = sessId;
        this.sid = sid;
        this.ttid = ttid;
        this.price = price;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getSessId() {
        return sessId;
    }

    public void setSessId(Integer sessId) {
        this.sessId = sessId;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getTtId() {
        return ttid;
    }

    public void setTtId(Integer ttid) {
        this.ttid = ttid;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
