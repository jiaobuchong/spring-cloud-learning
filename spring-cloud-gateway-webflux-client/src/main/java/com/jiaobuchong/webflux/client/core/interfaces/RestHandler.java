package com.jiaobuchong.webflux.client.core.interfaces;

import com.jiaobuchong.webflux.client.core.bean.MyMethodInfo;
import com.jiaobuchong.webflux.client.core.bean.ServerInfo;

// rest 请求调用 handler
public interface RestHandler {

    // 初始化服务器信息
    void init(ServerInfo serverInfo);

    // 调用 rest 请求，返回接口
    Object invokeRest(MyMethodInfo methodInfo);
}
