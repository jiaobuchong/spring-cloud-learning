package com.jiaobuchong.webflux.client;

import com.jiaobuchong.webflux.client.core.JdkProxyCreator;
import com.jiaobuchong.webflux.client.core.interfaces.ProxyCreator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringCloudWebfluxClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudWebfluxClientApplication.class, args);
    }

    @Bean
    ProxyCreator jdkProxyCreator() {
        return new JdkProxyCreator();
    }

    @Bean
    FactoryBean<IUserApi> userApi(ProxyCreator proxyCreator) {
        return new FactoryBean<IUserApi>() {
            // 返回代理上对象
            @Override
            public IUserApi getObject() throws Exception {
                return (IUserApi) proxyCreator.createProxy(getObjectType());
            }

            @Override
            public Class<?> getObjectType() {
                return IUserApi.class;
            }
        };
    }

}
