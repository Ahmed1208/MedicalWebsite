package com.example.dto;

import com.example.entity.Customer;
import java.time.LocalDate;

public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private LocalDate birthdate;
    private Customer.Gender gender;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Customer.Gender getGender() {
        return gender;
    }

    public void setGender(Customer.Gender gender) {
        this.gender = gender;
    }
}