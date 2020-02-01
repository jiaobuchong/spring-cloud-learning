package com.dn.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class CustomerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerDemoApplication.class, args);
    }




    // LoadBalanced 会去获取服务实例，使用 Ribbon 进行负载均衡
    @Bean
    @LoadBalanced
    public RestTemplate template(){
        return new RestTemplate();
    }

}
