package com.jiaobuchong.reactor.test;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * Created on 2021-02-09
 */
public class ReactorBackPressure {
    @Test
    public void testBackpressure() {
        Flux.range(1, 6)    // 1
                .doOnRequest(n -> System.out.println("Request " + n + " values..."))    // 2
                .subscribe(new BaseSubscriber<Integer>() {  // 3
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) { // 4
                        System.out.println("Subscribed and make a request...");
                        // 定义在订阅的时候执行的操作
                        request(1); // 5
                    }

                    @Override
                    protected void hookOnNext(Integer value) {  // 6
                        // 定义在每次收到一个元素时候的操作
                        try {
                            TimeUnit.SECONDS.sleep(1);  // 7
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Get value [" + value + "]");    // 8
                        // 每次处理完1个元素后再请求1个元素
                        request(1); // 9
                    }
                });
    }
}
