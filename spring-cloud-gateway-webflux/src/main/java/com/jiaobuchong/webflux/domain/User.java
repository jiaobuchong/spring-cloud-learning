package com.jiaobuchong.webflux.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//表
@Document("user")
@Data
public class User {

    @Id
    private String id;

    private String name;

    private int age;
}
