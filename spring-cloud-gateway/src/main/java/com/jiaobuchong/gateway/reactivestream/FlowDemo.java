package com.jiaobuchong.gateway.reactivestream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class FlowDemo {
    public static void main(String[] args) throws Exception {
        // 1. 定义发布者，发布的数据是 Integer
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        // 2. 定义订阅者
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<Integer>() {

            private Flow.Subscription subscription;

            // 建立订阅关系的时候会调用
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                // 保存订阅关系，需要用它来给发布者响应
                this.subscription = subscription;
                // 请求一个数据
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

        // 3. 发布者和订阅者建立订阅关系
        publisher.subscribe(subscriber);

        // 4. 生产数据并发布
        for (int i = 0; i < 1000; i++) {
            // submit 是一个阻塞方法，缓冲池满了的话，发布者就会被阻塞,
            // 生成 258 条数据就被阻塞了，阻塞了之后订阅者消费一条数据，发布者才会发布一条数据
            System.out.println("生产数据: " + i);
            publisher.submit(i);
        }


        // 5. 结束后关闭发布者, try-resource 获取 finally 正确关闭
        publisher.close();

        // 主线程延迟停止，否则数据没有消费就退出
        Thread.currentThread().join(60000);
    }
}
