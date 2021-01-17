package com.jiaobuchong.webflux.client;


import com.jiaobuchong.webflux.client.core.ApiServer;
import com.jiaobuchong.webflux.client.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ApiServer("http://localhost:8080/user")
public interface IUserApi {

    @GetMapping("/list")
    Flux<User> getAllUser();

    @GetMapping("/get/{id}")
    Mono<User> getUser(@PathVariable("id") String id);

    @GetMapping("/delete/{id}")
    Mono<Void> deleteUser(@PathVariable("id") String id);

    @PostMapping("/create")
    Mono<User> createUser(@RequestBody Mono<User> user);

}
