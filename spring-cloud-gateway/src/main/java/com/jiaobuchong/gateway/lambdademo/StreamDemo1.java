package com.jiaobuchong.gateway.lambdademo;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 并行流
 */
public class StreamDemo1 {
    public static void main(String[] args) throws Exception {
        // 串行打印的
//        long count = IntStream.range(1, 100).peek(StreamDemo1::debug).count();
        // 并行流，我机器是4核，每次会打印4个值
//        并行流使用的线程池是：ForkJoinPool.commonPool-worker-  线程个数跟cpu的核数一样

//        修改默认的线程数
//        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
//        long count1 = IntStream.range(1, 100).parallel().peek(StreamDemo1::debug).count();


        // 多次调用串行和并行的函数，以最后一次调用为准
//        long count1 = IntStream.range(1, 100)
//                .parallel().peek(StreamDemo1::debug)      // 调用 parallel 产生并行流
//                .sequential().peek(StreamDemo1::debug1)   // 调用 sequential 产生串行流
//                .count();


        // 使用自己的线程池，防止任务阻塞
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        forkJoinPool.submit(() -> {
            long count1 = IntStream.range(1, 100).parallel().peek(StreamDemo1::debug).count();
        });
        forkJoinPool.shutdown();

        synchronized (forkJoinPool) {
            forkJoinPool.wait();
        }










    }

    private static void debug(int i) {
        System.out.println(Thread.currentThread().getName() + " debug " + i);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void debug1(int i) {
        System.err.println("debug " + i);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
