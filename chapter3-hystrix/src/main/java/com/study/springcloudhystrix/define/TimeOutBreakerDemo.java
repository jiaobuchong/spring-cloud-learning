package com.study.springcloudhystrix.define;

import java.util.Random;
import java.util.concurrent.*;

public class TimeOutBreakerDemo {

    public static void main(String[] args) {
       ExecutorService executorService =  Executors.newSingleThreadExecutor();

       RandomCommand randomCommand = new RandomCommand();
       
       Future<String> future = executorService.submit(new Callable<String>() {

           @Override
           public String call() throws Exception {
               return randomCommand.run();
           }
       });
        String result = null;
        try {
             result = future.get(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
             result = randomCommand.fallback();

        }
        System.out.println("执行的结果是:"+result);
        executorService.shutdown();
    }



    private static Random random = new Random();
    public static class RandomCommand implements Command<String>{

        @Override
        public String run() {
           //休眠时间
            int timeOut = random.nextInt(150);
            System.out.println("休眠时间:"+timeOut);

            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "正常返回";
        }

        @Override
        public String fallback() {
            return "出错了，超时!!!";
        }
    }
}
