package com.study.springcloudfeign.fallback;

import com.study.springcloudfeign.service.HelloDemoService;
import org.springframework.stereotype.Component;

@Component
public class HelloDemoFallback implements HelloDemoService {
    @Override
    public String index() {
        return "出错了，降级...";
    }

    @Override
    public String getName(String name) {
        return "超时，没有名字...";
    }

    @Override
    public String testName(String name) {
        return "Post请求出错，降级...";
    }
}
