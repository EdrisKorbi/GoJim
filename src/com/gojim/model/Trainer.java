package com.gojim.model;

public class Trainer {
    private int id;
    private String fullName;
    private String specialty;
    private String experience;

    public Trainer(int id, String fullName, String specialty, String experience) {
        this.id = id;
        this.fullName = fullName;
        this.specialty = specialty;
        this.experience = experience;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getExperience() {
        return experience;
    }
}