package com.jiaobuchong.webflux.handler;

import com.jiaobuchong.webflux.domain.User;
import com.jiaobuchong.webflux.repository.UserRepository;
import com.jiaobuchong.webflux.utils.CheckUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private UserRepository userRepository;

    // 这种比起 Autowired 会和 spring 耦合度低一些
    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 获取所有用户
    public Mono<ServerResponse> getAllUser(ServerRequest request) {

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(userRepository.findAll(), User.class);
    }

    // 创建用户
    public Mono<ServerResponse> createUser(ServerRequest request) {
        // 获取用户传参
        Mono<User> user = request.bodyToMono(User.class);
        // 不要使用这种
//        user.block();
        // 操作对象，返回另外一个对象
        // Mono 和 Flux 任何时候都是一个流，我们在代码中不能直接去消费它
        // 这个消费必须通过 Spring boot 来消费
        return user.flatMap(u -> {
            // 校验代码需要放在这里
            CheckUtils.checkName(u.getName());
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(userRepository.saveAll(user), User.class);
        });

//        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
//                .body(userRepository.saveAll(user), User.class);
    }

    // 删除用户
    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        // 获取用户传参
        String id = request.pathVariable("id");
        return userRepository.findById(id)
                .flatMap(user -> this.userRepository.delete(user)
                        .then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
