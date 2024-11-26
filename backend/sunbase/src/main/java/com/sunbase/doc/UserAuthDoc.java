package com.sunbase.doc;

import com.sunbase.dtos.CustomerRequest;
import com.sunbase.utils.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.sunbase.constants.AppConstants.*;

@RequestMapping(AUTH)
public interface UserAuthDoc {

    @PostMapping("/register")
    ResponseEntity<BaseResponse<String>> register(@RequestBody CustomerRequest request);

    @PostMapping("/login")
    ResponseEntity<BaseResponse<String>> login(@RequestBody CustomerRequest request);
}
