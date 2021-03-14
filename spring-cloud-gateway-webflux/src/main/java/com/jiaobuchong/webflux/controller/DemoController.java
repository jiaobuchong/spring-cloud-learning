package com.jiaobuchong.webflux.controller;

import com.jiaobuchong.webflux.domain.CommonResponse;
import com.jiaobuchong.webflux.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/index")
    public CommonResponse<User> index() {
        log.info("mvc start");
        CommonResponse<User> commonResponse = getUser();
        log.info("mvc end");
        return commonResponse;
    }

    @RequestMapping("/index2")
    public Mono<CommonResponse<User>> index2() {
        User user = new User();
        user.setName("jack");
        user.setAge(24);
        user.setId("sdgdsg");

        CommonResponse<User> commonResponse = new CommonResponse<>();
        commonResponse.setCode(0);
        commonResponse.setMessage("ok");
        commonResponse.setData(user);
        return Mono.just(commonResponse);
    }

    /**
     * Mono 和 Flux 都是一个发布者
     * @return
     */
    @RequestMapping("/index3")
    public Mono<CommonResponse<User>> index3() {
        log.info("flux start");
        Mono<CommonResponse<User>> commonResponseMono = Mono.fromSupplier(this::getUser);
        log.info("flux end");
        return commonResponseMono;
    }

    @RequestMapping(value = "/index4", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> index4() {
        log.info("flux start");
        Flux<String> result = Flux.fromStream(IntStream.range(1, 5)
                .mapToObj(i -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "flux times" + i;
                }));
        log.info("flux end");
        return result;
    }


    public CommonResponse<User> getUser() {

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setName("jack");
        user.setAge(24);
        user.setId("sdgdsg");

        CommonResponse<User> commonResponse = new CommonResponse<>();
        commonResponse.setCode(0);
        commonResponse.setMessage("ok");
        commonResponse.setData(user);
        return commonResponse;
    }
}
