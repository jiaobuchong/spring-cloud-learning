package com.jiaobuchong.reactor.test;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * Created on 2021-02-09
 */
public class WebClientDemo {

    @Test
    public void webClientTest1() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");   // 1
        Mono<String> resp = webClient
                .get().uri("/hello") // 2
                .retrieve() // 3
                .bodyToMono(String.class);  // 4
        resp.subscribe(System.out::println);    // 5
        TimeUnit.SECONDS.sleep(1);  // 6
    }

}
