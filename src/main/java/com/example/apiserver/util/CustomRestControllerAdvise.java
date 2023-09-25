package com.example.apiserver.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class CustomRestControllerAdvise{
//
//    @ExceptionHandler(BindException.class)
//    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
//    public ResponseEntity<Map<String, String>> handleBindException(BindException e){
//        log.error(e.getMessage());
//
//        Map<String, String> errorMap = new HashMap<>();
//
//
//        return null;
//    }
}
