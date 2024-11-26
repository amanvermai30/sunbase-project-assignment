package com.sunbase.services;

import com.sunbase.dtos.CustomerRequest;
import com.sunbase.dtos.CustomerResponse;
import com.sunbase.jpa.model.Customer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerResponse saveCustomer(CustomerRequest request);

    CustomerResponse getCustomerById(String uuid);

    Optional<Customer> getCustomerByEmail(String email);

    Customer getLoggedInCustomer();

    CustomerResponse deleteCustomer(String uuid);

    List<CustomerResponse> getAllCustomers();

    List<CustomerResponse> fetchDataFromServer() throws IOException;
}
