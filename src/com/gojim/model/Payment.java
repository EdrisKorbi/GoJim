package com.gojim.model;

public class Payment {
    private int id;
    private String memberName;
    private double amount;
    private String paymentDate;
    private String status;

    public Payment(int id, String memberName, double amount, String paymentDate, String status) {
        this.id = id;
        this.memberName = memberName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getMemberName() {
        return memberName;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }
}