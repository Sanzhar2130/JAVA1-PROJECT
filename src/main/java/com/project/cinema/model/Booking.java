package com.project.cinema.model;

import java.time.LocalDateTime;

public class Booking {
    private Integer bid;
    private Integer clientId;
    private String bookingCode;
    private LocalDateTime createdAt;
    private String status;
    private String paymentMethod;
    private Double totalPrice;

    public Booking() {
    }

    public Booking(Integer clientId, String bookingCode, LocalDateTime createdAt, String status) {
        this.clientId = clientId;
        this.bookingCode = bookingCode;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Booking(Integer bid, Integer clientId, String bookingCode, LocalDateTime createdAt, String status, String paymentMethod, Double totalPrice) {
        this.bid = bid;
        this.clientId = clientId;
        this.bookingCode = bookingCode;
        this.createdAt = createdAt;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
