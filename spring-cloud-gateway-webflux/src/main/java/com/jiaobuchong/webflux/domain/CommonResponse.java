package com.jiaobuchong.webflux.domain;

import lombok.Data;

@Data
public class CommonResponse<T> {

    private int code;

    private String message;

    private T data;

}
