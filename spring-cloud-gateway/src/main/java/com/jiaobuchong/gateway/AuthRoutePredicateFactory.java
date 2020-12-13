package com.jiaobuchong.gateway;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class AuthRoutePredicateFactory extends AbstractRoutePredicateFactory<AuthRoutePredicateFactory.Config> {

    public AuthRoutePredicateFactory() {
        super(Config.class);
    }

    //    Auth=Authorization,token
    // 通过 Auth 可以定位到是哪个 Predicate，shortcutFieldOrder 指定了字段名称的顺序
    // 下面两个成员变量指定的 Config 的字段名称
    private static final String NAME_KEY = "name";
    private static final String VALUE_KEY = "value";

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(NAME_KEY, VALUE_KEY);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        //Header中携带了某个值，进行header的判断
        return (exchange -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            List<String> headerList = headers.get(config.getName());
            return headerList.size() > 0;
        });
    }

    public static class Config {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
