package com.dn.hellodemo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class HelloDemoPeer1Application {

    public static void main(String[] args) {
        SpringApplication.run(HelloDemoPeer1Application.class, args);
    }


    @GetMapping("")
    public Object index() throws InterruptedException {
        int timeout = ThreadLocalRandom.current().nextInt(150);
        Thread.sleep(timeout);
        System.out.println(timeout);
        String str = "这是服务端1返回的应答";
        return new String(str);
    }


}
