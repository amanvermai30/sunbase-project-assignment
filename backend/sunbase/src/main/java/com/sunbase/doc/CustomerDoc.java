package com.sunbase.doc;
import com.sunbase.dtos.CustomerRequest;
import com.sunbase.dtos.CustomerResponse;
import com.sunbase.utils.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.sunbase.constants.AppConstants.*;

@RequestMapping(APP_NAME)
public interface CustomerDoc {

    @PostMapping(PRIVATE_ROUTE_TYPE + SAVE)
    ResponseEntity<BaseResponse<CustomerResponse>> createCustomer(@RequestBody CustomerRequest request);

    @PutMapping(PRIVATE_ROUTE_TYPE + UPDATE)
    ResponseEntity<BaseResponse<CustomerResponse>> updateCustomer(@RequestBody CustomerRequest request);

    @DeleteMapping(PRIVATE_ROUTE_TYPE + DELETE)
    ResponseEntity<BaseResponse<CustomerResponse>> deleteCustomer(@RequestParam String uuid);

    @GetMapping(PRIVATE_ROUTE_TYPE + GET_BY_ID)
    ResponseEntity<BaseResponse<CustomerResponse>> getCustomerById(@RequestParam String uuid);

    @GetMapping(PRIVATE_ROUTE_TYPE + GET_ALL)
    ResponseEntity<BaseResponse<List<CustomerResponse>>> getAllCustomer();

    @GetMapping(PRIVATE_ROUTE_TYPE + GET_ALL + SUNBASE_SERVER)
    ResponseEntity<BaseResponse<List<CustomerResponse>>> fetchDataFromServer() throws IOException;
}
