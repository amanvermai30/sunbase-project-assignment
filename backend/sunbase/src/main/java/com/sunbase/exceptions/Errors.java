package com.sunbase.exceptions;

import lombok.Data;

@Data
public class Errors {
    private String appCode;
    private String appMessage;
    private String appFnQ;
}
