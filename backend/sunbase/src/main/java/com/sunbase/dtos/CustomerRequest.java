package com.sunbase.dtos;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CustomerRequest {

    private String id;
    private String firstName;
    private String lastName;
    private String street;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;
    private String password;

}
