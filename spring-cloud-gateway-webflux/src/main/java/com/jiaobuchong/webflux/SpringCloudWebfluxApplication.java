package com.jiaobuchong.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class SpringCloudWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudWebfluxApplication.class, args);
    }

}
