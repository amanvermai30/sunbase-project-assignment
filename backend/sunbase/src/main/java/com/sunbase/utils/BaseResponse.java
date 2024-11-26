package com.sunbase.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse<T> {
    private T payload;
    private String message;
    private Integer code;

    public static <T> BaseResponse<T> createSuccessResponse(T payload) {
        return BaseResponse.<T>builder()
                .payload(payload)
                .message("SUCCESS")
                .code(200)
                .build();
    }
}
