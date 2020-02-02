package com.study.springcloudhystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.study.springcloudhystrix.command.CustomerCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping("index")
    public Object getIndex() throws InterruptedException {

        return new CustomerCommand(restTemplate).execute();
     }

     @HystrixCommand(fallbackMethod = "callTimeoutFallback",
      threadPoolProperties = {
             @HystrixProperty(name="coreSize",value = "1"),
              @HystrixProperty(name="queueSizeRejectionThreshold",value = "1")
      },
     commandProperties = {
             @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "100")
     })
     @GetMapping("index2")
     public Object getIndex2(){
        return restTemplate.getForObject("http://helloserver",String.class,"");
     }

     public Object callTimeoutFallback(){
        return  "请求index2降级";
     }


}
