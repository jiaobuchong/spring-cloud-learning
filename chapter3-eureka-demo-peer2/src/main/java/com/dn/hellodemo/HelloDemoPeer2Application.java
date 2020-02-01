package com.dn.hellodemo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@RestController
@RefreshScope
public class HelloDemoPeer2Application {

    public static void main(String[] args) {
        SpringApplication.run(HelloDemoPeer2Application.class, args);
    }

    @GetMapping("")
    public Object index(){
        String str =  "这是服务端2返回的应答";
        return new String(str);
    }

}
