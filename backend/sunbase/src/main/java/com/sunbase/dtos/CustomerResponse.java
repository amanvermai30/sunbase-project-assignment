package com.sunbase.dtos;

import lombok.Data;

@Data
public class CustomerResponse {

    private String uuid;
    private String firstName;
    private String lastName;
    private String street;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;

}
