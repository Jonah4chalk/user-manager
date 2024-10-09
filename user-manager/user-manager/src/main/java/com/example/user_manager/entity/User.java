package com.example.user_manager.entity;

import com.sanctionco.jmail.JMail;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String username;
    private String first_name;
    private String last_name;
    private String email_address;
    private String phone_number;

    public User(){}

    public User(String username, String first_name, String last_name, String email_address, String phone_number) throws IllegalArgumentException {
        if (JMail.isInvalid(email_address)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        else {
            this.username = username;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email_address = email_address;
            this.phone_number = phone_number;
        }
        
    }

    // getters and setters

    public Long getId() {
        return user_id;
    }

    public void setId(Long new_id) {
        this.user_id = new_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String new_username) {
        this.username = new_username;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String new_first_name) {
        this.first_name = new_first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String new_last_name) {
        this.last_name = new_last_name;
    }

    public String getEmailAddress() {
        return email_address;
    }

    public void setEmailAddress(String new_email) {
        if (JMail.isInvalid(new_email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        else {
            this.email_address = new_email;
        }
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String new_phone_number) {
        this.phone_number = new_phone_number;
    }
}
