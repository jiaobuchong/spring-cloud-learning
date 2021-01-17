package com.jiaobuchong.webflux.utils;

import com.jiaobuchong.webflux.exception.CheckException;

import java.util.stream.Stream;

public class CheckUtils {

    private static final String[] INVALID_NAMES = {"admin", "guanliyuan"};
    /**
     * 校验名字，不成功抛出异常
     * @param value
     */
    public static void checkName(String value) {
        Stream.of(INVALID_NAMES).filter(name -> name.equalsIgnoreCase(value))
                .findAny().ifPresent(name -> {
                    throw new CheckException("name", name);
        });
    }
}
