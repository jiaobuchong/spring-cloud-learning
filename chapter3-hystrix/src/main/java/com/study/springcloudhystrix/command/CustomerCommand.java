package com.study.springcloudhystrix.command;

import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

public class  CustomerCommand extends HystrixCommand<Object> {

  private RestTemplate restTemplate;
    public CustomerCommand(RestTemplate restTemplate) {

        super(
       Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("study-hystrix"))
               .andCommandKey(HystrixCommandKey.Factory.asKey("CustomerController"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("studyThreadPool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                              .withExecutionTimeoutInMilliseconds(100)
                                               .withCircuitBreakerSleepWindowInMilliseconds(5000))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                                                 .withCoreSize(1)
                                                 .withMaxQueueSize(2))
        );
        this.restTemplate = restTemplate;
    }

    @Override
    protected Object run() throws Exception {
     //核心实现，调用我们期望调用的方法.
        System.out.println("当前线程是: "+Thread.currentThread().getName());
      Object result = restTemplate.getForObject("http://helloserver",String.class,"");
      return  result;
    }

    @Override
    protected Object getFallback() {
        //核心方法，降级之后会来实现这个
        System.out.println("降级啦。。。");
        return "降级了，返回降级";
    }
}
