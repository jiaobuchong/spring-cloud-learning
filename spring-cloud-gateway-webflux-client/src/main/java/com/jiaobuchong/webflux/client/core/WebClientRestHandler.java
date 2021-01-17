package com.jiaobuchong.webflux.client.core;

import com.jiaobuchong.webflux.client.core.bean.MyMethodInfo;
import com.jiaobuchong.webflux.client.core.bean.ServerInfo;
import com.jiaobuchong.webflux.client.core.interfaces.RestHandler;

public class WebClientRestHandler implements RestHandler {
    @Override
    public void init(ServerInfo serverInfo) {

    }

    @Override
    public Object invokeRest(MyMethodInfo methodInfo) {
        return null;
    }
}
