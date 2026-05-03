package com.gojim.model;

/**
 * Represents one subscription plan from the database.
 */
public class Subscription {

    private int id;
    private String planName;
    private int durationDays;
    private double price;

    public Subscription(int id, String planName, int durationDays, double price) {
        this.id = id;
        this.planName = planName;
        this.durationDays = durationDays;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getPlanName() {
        return planName;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Important: this controls how the object appears inside combo boxes.
     */
    @Override
    public String toString() {
        return planName;
    }
}