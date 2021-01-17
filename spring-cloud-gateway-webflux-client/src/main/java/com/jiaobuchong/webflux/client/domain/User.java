package com.jiaobuchong.webflux.client.domain;

import lombok.Builder;
import lombok.Data;

//表
@Data
@Builder
public class User {

    private String id;

    private String name;

    private int age;
}
