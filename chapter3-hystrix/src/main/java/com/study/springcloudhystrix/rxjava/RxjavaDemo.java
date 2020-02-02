package com.study.springcloudhystrix.rxjava;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class RxjavaDemo {

    public static void main(String[] args) throws InterruptedException {

        Single.just(new Action1().init())
                .subscribeOn(Schedulers.newThread())  // 开启新线程去处理，放到不同的线程上去运作
                .subscribe(RxjavaDemo::println);

        Single.fromCallable(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "ok1";
                    }
                }
        ).subscribeOn(Schedulers.io())
                .subscribe(RxjavaDemo::isOk);

        observeremo();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static class Action1 {

        public String init() {
            return "事件1发布";
        }


    }

    public static void println(Object value) {
        //我们可以通过rxjava这样的东西拿到在某一个事件进行发布时的value
        System.out.printf("[线程: %s] 数据: %s\n", Thread.currentThread().getName(), value);

    }

    public static void isOk(Object val) {
        if (val.equals("ok")) {
            System.out.println("执行了XXXX");
        } else {
            System.out.println("不通过");
        }
    }

    public static void observeremo() throws InterruptedException {
        List<Integer> valus = Arrays.asList(1, 2, 3, 4);
        Observable.from(valus).
                subscribe(
                        next -> {
                            if (next > 2) {
                                throw new IllegalArgumentException("非法参数");
                            }
                            System.out.println("处理数据" + next);
                        },
                        error -> {
                            System.out.println("降级!!");
                        },
                        () -> {
                            System.out.println("执行完毕");
                        }
                );

    }
}
