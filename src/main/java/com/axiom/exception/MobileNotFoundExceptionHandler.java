package com.axiom.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.axiom.util.ErrorConstants;

@RestControllerAdvice
public class MobileNotFoundExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileNotFoundExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public Object handleException(Exception exception) {
        LOGGER.error("Something went wrong: ", exception);
        return new ErrorInfo(ErrorConstants.SERVICE_EXCEPTION, ErrorConstants.SERVICE_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler({MobileNotFoundException.class})
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public Object handleCustomException(MobileNotFoundException ex) {
        LOGGER.error("ApiException thrown: ", ex);
        return new ErrorInfo(ErrorConstants.MOBILE_NOT_FOUND, ErrorConstants.MOBILE_NOT_FOUND_MESSAGE);
    }

}
