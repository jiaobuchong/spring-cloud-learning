package com.study.springcloudfeign.controller;

import com.study.springcloudfeign.service.HelloDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CustomerController {

    @Autowired
  HelloDemoService helloDemoService;


    @GetMapping("index")
    public Object getIndex(){
        return helloDemoService.index();
     }

    @GetMapping("/find-user")
    public Object getUser(@RequestParam("name")String name){
        return helloDemoService.getName(name);
    }

    @GetMapping("/set-user")
    public Object setUser(@RequestParam("name")String name){
        return helloDemoService.getName(name);
    }


}
