package com.undrostres.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
    private String msg;
    private boolean success;
    private T data;
}
