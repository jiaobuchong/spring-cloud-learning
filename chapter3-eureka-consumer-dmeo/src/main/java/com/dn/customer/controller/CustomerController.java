package com.dn.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("index")
    public Object getIndex() {
        // 这里发起 http 请求
        return restTemplate.getForObject("http://HELLOSERVER/", String.class, "");
    }


}
