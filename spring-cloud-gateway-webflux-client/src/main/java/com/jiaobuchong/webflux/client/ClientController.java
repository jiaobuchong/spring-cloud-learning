package com.jiaobuchong.webflux.client;

import com.jiaobuchong.webflux.client.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ClientController {

    // 直接注入实现的接口
    @Autowired
    IUserApi userApi;

    @GetMapping("/")
    public void getAllUser() {
        // 不订阅，不会发生请求，但会进入处理类
//        userApi.getAllUser();
//        userApi.getUser("23542");
//        userApi.deleteUser("235423");
//        userApi.createUser(Mono.just(User.builder().name("jackchou").age(24).build()));

//        Flux<User> users = userApi.getAllUser();
//        users.subscribe(System.out::println);

        String id = "6003efd688b89b64472023da";
        userApi.getUser(id).subscribe(user -> {
            System.out.println("getUserById: "+ user);
        });

        // 删除
//        userApi.deleteUser(id).subscribe();

        // 创建用户
        userApi.createUser(Mono.just(User.builder().name("jiaobuchong").age(24).build()))
                .subscribe(System.out::println);
    }

    // 处理异常信息
    @GetMapping("/exception/deal")
    public void dealException() {

        String id = "6003efd688b89b64472023da1222";
        userApi.getUser(id).subscribe(user -> {
            System.out.println("getUserById: "+ user);
        }, e -> {
            System.err.println("user not found" + e.getMessage());
        });

    }


}
