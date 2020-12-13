package com.dn.hellodemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/gateway")
public class GatewayTestController {

    @GetMapping("/test")
    public String test() {
        return "gateway test " + ThreadLocalRandom.current().nextInt();
    }
}
