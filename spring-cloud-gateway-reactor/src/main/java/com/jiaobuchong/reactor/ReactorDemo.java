package com.jiaobuchong.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

public class ReactorDemo {
    public static void main(String[] args) {
        // reactor = jdk8 stream + jdk9 reactive stream
        // Mono 0-1 个元素
        // Flux 0-N 个元素
        String[] strs = {"1", "2", "3"};

        // 2. 定义订阅者
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {

            private Subscription subscription;

            // 建立订阅关系的时候会调用
            @Override
            public void onSubscribe(Subscription subscription) {
                // 保存订阅关系，需要用它来给发布者响应
                this.subscription = subscription;
                // 向发布者请求一个数据
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer integer) {
                // 接收到一个数据并处理
                System.out.println("接收到数据: " + integer);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 处理完调用 request 再请求一个数据，调节发布者产生数据的频率
                this.subscription.request(1);
                // 已经到达了目标，调用 cancel 告诉发布者订阅者不再接受数据了
//                this.subscription.cancel();
            }

            @Override
            public void onError(Throwable throwable) {

                // onNext 里处理数据出异常了，这里就会监听到
                throwable.printStackTrace();
                // 出错了告诉发布者不再接受数据
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                // 全部处理完，发布者关闭了 publisher.close();
                System.out.println("处理完了！");
            }
        };

        // jdk8 的 stream 操作
        Flux.fromArray(strs).map(s -> Integer.parseInt(s))
                // 最终操作
                // 这里是 jdk9 的响应式流
                .subscribe(subscriber);
    }

}
