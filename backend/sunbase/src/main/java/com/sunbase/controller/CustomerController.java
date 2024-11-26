package com.sunbase.controller;

import com.sunbase.doc.CustomerDoc;
import com.sunbase.dtos.CustomerRequest;
import com.sunbase.dtos.CustomerResponse;
import com.sunbase.services.CustomerService;
import com.sunbase.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CustomerController implements CustomerDoc {


    @Autowired
    private CustomerService customerService;


    @Override
    public ResponseEntity<BaseResponse<CustomerResponse>> createCustomer(CustomerRequest request) {
        return ResponseEntity.ok(BaseResponse.createSuccessResponse(customerService.saveCustomer(request)));
    }

    @Override
    public ResponseEntity<BaseResponse<CustomerResponse>> updateCustomer(CustomerRequest request) {
        return ResponseEntity.ok(BaseResponse.createSuccessResponse(customerService.saveCustomer(request)));
    }

    @Override
    public ResponseEntity<BaseResponse<CustomerResponse>> deleteCustomer(String uuid) {
        return ResponseEntity.ok(BaseResponse.createSuccessResponse(customerService.deleteCustomer(uuid)));
    }

    @Override
    public ResponseEntity<BaseResponse<CustomerResponse>> getCustomerById(String uuid) {
        return ResponseEntity.ok(BaseResponse.createSuccessResponse(customerService.getCustomerById(uuid)));
    }

    @Override
    public ResponseEntity<BaseResponse<List<CustomerResponse>>> getAllCustomer() {
        return ResponseEntity.ok(BaseResponse.createSuccessResponse(customerService.getAllCustomers()));
    }

    @Override
    public ResponseEntity<BaseResponse<List<CustomerResponse>>> fetchDataFromServer() throws IOException {
        return ResponseEntity.ok(BaseResponse.createSuccessResponse(customerService.fetchDataFromServer()));
    }
}
