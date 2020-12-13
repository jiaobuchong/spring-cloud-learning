package com.dn.hellodemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/create")
    public String test() {
        return "create order id: " + ThreadLocalRandom.current().nextInt();
    }
}
