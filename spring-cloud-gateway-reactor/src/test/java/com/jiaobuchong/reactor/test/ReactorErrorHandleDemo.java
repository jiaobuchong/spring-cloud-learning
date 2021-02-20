package com.jiaobuchong.reactor.test;

import java.util.Random;

import com.jiaobuchong.reactor.ServiceException;
import org.junit.jupiter.api.Test;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created on 2021-02-08
 */
public class ReactorErrorHandleDemo {

    @Test
    public void testErrorHandling() {
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3)) // 1
                .map(i -> i * i)
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void testErrorToDefault() {
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .onErrorReturn(0)   // 出现错误返回一个默认值
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        Flux.range(1, 6)
                .map(i -> 10/(i-3))
                .onErrorResume(e -> Mono.just(new Random().nextInt(6))) // 提供新的数据流
                .map(i -> i*i)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void testThrowServiceException() {
        Flux.just("timeout")
                .flatMap(k -> {
                    if (k.equals("timeout")) {
                        throw new RuntimeException("timeout");
                    }
                    return Flux.just("hhhh");
                })
                // 包装成另外一个业务异常并直接往上抛出
//                .onErrorMap(e -> AdServiceException.of(400, "se", e))
                .onErrorResume(e -> Flux.error(ServiceException.of("business exception", e)))
                .subscribe(System.out::println);
    }

    @Test
    public void testLogError() {
        Flux.just("timeout")
                .flatMap(k -> {
                    if (k.equals("timeout")) {
                        throw new RuntimeException("timeout");
                    }
                    return Flux.just("hhhh");
                })
                // 记录错误日志，然后错误会继续向下传递
                .doOnError(e -> {
                    e.printStackTrace();
                })
                .onErrorResume(e -> Flux.just("errorResume"))
                .subscribe(System.out::println);
    }


}
