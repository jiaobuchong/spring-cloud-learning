package com.jiaobuchong.reactor;

public class ServiceException extends RuntimeException {

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ServiceException of(String message, Throwable cause) {
        return new ServiceException(message, cause);
    }
}
