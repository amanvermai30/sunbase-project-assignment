package com.sunbase.controller;

import com.sunbase.doc.UserAuthDoc;
import com.sunbase.dtos.CustomerRequest;
import com.sunbase.services.AuthService;
import com.sunbase.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthController implements UserAuthDoc {


    @Autowired
    private AuthService authService;


    @Override
    public ResponseEntity<BaseResponse<String>> register(CustomerRequest request) {
        return ResponseEntity.ok(BaseResponse.createSuccessResponse(authService.register(request)));
    }

    @Override
    public ResponseEntity<BaseResponse<String>> login(CustomerRequest request) {
        return ResponseEntity.ok(BaseResponse.createSuccessResponse(authService.login(request)));
    }
}
