package com.example.aptProject.entity;

public class LocationCode {
    private int lCode;
    private String firstName;
    private String secondName;
    private String lastName;

    @Override
    public String toString() {
        return "LocationCode{" +
                "lCode=" + lCode +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public int getlCode() {
        return lCode;
    }

    public void setlCode(int lCode) {
        this.lCode = lCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocationCode() {
    }

    public LocationCode(int lCode, String firstName, String secondName, String lastName) {
        this.lCode = lCode;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
    }
}
