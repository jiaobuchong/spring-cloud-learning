package com.jiaobuchong.gateway.reactivestream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * 过滤数据和加工
 * 输入源数据 Integer, 过滤掉小于 0 的，然后转换成字符串发布出去
 */
class MyProcessor extends SubmissionPublisher<String>
        implements Flow.Processor<Integer, String> {
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
        System.out.println("处理器接收到数据: " + integer);
        // 处理完调用 request 再请求一个数据，调节发布者产生数据的频率
        if (integer > 5) {
            this.submit("转换后的数据:" + integer);
        }
        // 处理完调用, request 再请求下一个数据
        this.subscription.request(1);
        // 已经到达了目标，调用 cancel 告诉发布者订阅者不再接受数据了
//                this.subscription.cancel();
    }

    @Override
    public void onError(Throwable throwable) {

        // onNext 里处理数据出异常了，这里就会监听到
        throwable.printStackTrace();
        // 告诉发布者，后面不接收数据了
        this.subscription.cancel();
    }

    @Override
    public void onComplete() {
        // 全部处理完，发布者关闭了 publisher.close();
        System.out.println("处理器处理完了！");

        this.close();
    }
}

public class FlowDemo1 {

    public static void main(String[] args) throws InterruptedException {
        // 1. 定义发布者，发布的数据是 Integer
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        // 2. 定义处理器，对数据进行过滤，并转换为String类型
        // MyProcessor 即是生产者又是消费者
        MyProcessor myProcessor = new MyProcessor();
        // 3. 发布者和处理器建立订阅关系, 发布者先将数据发到处理器
        publisher.subscribe(myProcessor);  // MyProcessor 是消费者


        // 4. 定义最终订阅者，消费 String 类型数据
        Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {

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
            public void onNext(String item) {
                // 接收到一个数据并处理
                System.out.println("接收到数据: " + item);
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

        // 5. 处理器和最终订阅者建立订阅关系，处理器处理完，再发送给最终订阅者
        myProcessor.subscribe(subscriber);  // MyProcessor 是发布者

        // 6. 生产数据并发布
        // 数据发布到处理器 -> 最终订阅者
        for (int i = 0; i < 10; i++) {
            publisher.submit(i);
        }


        // 7. 结束后关闭发布者, try-resource 获取 finally 正确关闭
        publisher.close();

        // 主线程延迟停止，否则数据没有消费就退出
        Thread.currentThread().join(6000);
    }

}
