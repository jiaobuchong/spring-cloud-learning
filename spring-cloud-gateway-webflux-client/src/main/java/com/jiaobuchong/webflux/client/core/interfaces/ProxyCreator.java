package com.jiaobuchong.webflux.client.core.interfaces;

// 创建代理类的接口
public interface ProxyCreator {

    // 创建代理对象
    Object createProxy(Class<?> cls);


}
