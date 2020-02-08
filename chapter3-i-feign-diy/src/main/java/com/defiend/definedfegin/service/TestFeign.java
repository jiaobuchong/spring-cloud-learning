package com.defiend.definedfegin.service;


import com.defiend.definedfegin.annotation.FeginClinet;
import com.defiend.definedfegin.annotation.FeginGet;
import org.springframework.stereotype.Component;

@Component
@FeginClinet(baseUrl = "http://www.baidu.com:80")
public interface TestFeign {

    @FeginGet(url = "/index.html")
    Object getSomeThing();
}
