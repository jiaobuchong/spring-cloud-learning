package com.jiaobuchong.webflux.client.core.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Map;

// 方法调用信息类
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyMethodInfo {

    // 请求 url
    private String url;

    // 请求方法
    private HttpMethod method;

    // 请求参数 url 里的
    private Map<String, Object> params;

    private Mono<?> body;

    // body 的类型
    private Class<?> bodyElementType;

    // 返回是 Mono 还是 Flux
    private boolean returnFlux;

    // 返回对象类型
    private Class<?> returnElementType;


}
