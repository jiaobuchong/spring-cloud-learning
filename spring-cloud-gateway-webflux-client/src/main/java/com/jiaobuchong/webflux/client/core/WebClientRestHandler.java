package com.jiaobuchong.webflux.client.core;

import com.jiaobuchong.webflux.client.core.bean.MyMethodInfo;
import com.jiaobuchong.webflux.client.core.bean.ServerInfo;
import com.jiaobuchong.webflux.client.core.interfaces.RestHandler;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientRestHandler implements RestHandler {
    private WebClient client;

    // 初始化 web client
    @Override
    public void init(ServerInfo serverInfo) {
        this.client = WebClient.create(serverInfo.getUrl());

    }

    // 处理 rest 请求
    @Override
    public Object invokeRest(MyMethodInfo methodInfo) {
        // 返回结果
        Object result = null;
        WebClient.RequestBodySpec request = this.client
                // 请求方法
                .method(methodInfo.getMethod())
                // 请求url 和 参数
                .uri(methodInfo.getUrl(), methodInfo.getParams())
                .accept(MediaType.APPLICATION_JSON);

        // 判断是否带了 requestBody
        WebClient.ResponseSpec retrieve;
        if (methodInfo.getBody() != null) {
            // 发出请求
            retrieve = request.body(methodInfo.getBody(), methodInfo.getBodyElementType()).retrieve();
        } else {
            retrieve = request.retrieve();
        }

        // 处理 body
        if (methodInfo.isReturnFlux()) {
            result = retrieve.bodyToFlux(methodInfo.getReturnElementType());
        } else {
            result = retrieve.bodyToMono(methodInfo.getReturnElementType());
        }
        return result;
    }
}
