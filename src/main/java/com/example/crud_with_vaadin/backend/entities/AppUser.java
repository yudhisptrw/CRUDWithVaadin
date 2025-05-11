package com.example.crud_with_vaadin.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AppUser {
    @Id
    @GeneratedValue
    private long id;

    private String firstName;
    private String lastName;
    private String address;
    private String job;

    //Default Constructor
    public AppUser() {
    }

    public AppUser(String firstName, String lastName, String address, String job) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.job = job;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getId() {
        return id;
    }


    @Override
    public String toString() {
        return String.format("AppUser[id=%d, firstName='%s', lastName='%s']", id,
                firstName, lastName);
    }
}
