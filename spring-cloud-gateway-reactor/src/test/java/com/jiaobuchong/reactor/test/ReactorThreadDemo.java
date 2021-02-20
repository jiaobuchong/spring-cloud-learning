package com.jiaobuchong.reactor.test;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Created on 2021-02-08
 */
public class ReactorThreadDemo {

    private String getStringSync() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, Reactor!";
    }

    @Test
    public void testSyncToAsync() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(this::getStringSync)    // 1
                // 将任务调度到 Schedulers 内置的弹性线程池执行，弹性线程池会为 Callable 的执行任务分配一个单独的线程
                .subscribeOn(Schedulers.elastic())  // 2
                .subscribe(System.out::println, null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }
}
