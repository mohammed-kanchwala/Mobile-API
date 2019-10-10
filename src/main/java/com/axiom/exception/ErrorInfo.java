package com.axiom.exception;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = -7834840446409811718L;
    String code;
    String message;

    ErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
