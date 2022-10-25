package com.undrostres.auth.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.undrostres.auth.models.Response;

public class HttpUtils {
    public static <T> ResponseEntity<Response<T>> createResponseEntity(String msg, boolean success, T data, HttpStatus status) {
        return new ResponseEntity<Response<T>>(new Response<T>(msg, success, data), status);
    }
}
