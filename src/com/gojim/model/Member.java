package com.gojim.model;

public class Member {
    private int id;
    private String fullName;
    private int age;
    private String phone;
    private int subscriptionId;
    private String subscriptionName;
    private String startDate;
    private String endDate;
    private String status;

    public Member(int id, String fullName, int age, String phone,
            int subscriptionId, String subscriptionName,
            String startDate, String endDate, String status) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.phone = phone;
        this.subscriptionId = subscriptionId;
        this.subscriptionName = subscriptionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }
}