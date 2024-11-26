package com.sunbase.services;

import com.sunbase.dtos.CustomerRequest;

public interface AuthService {

    String register(CustomerRequest request);
    String login(CustomerRequest request);
}
