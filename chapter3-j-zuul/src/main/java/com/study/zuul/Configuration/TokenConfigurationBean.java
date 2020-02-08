package com.study.zuul.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("stduy.zuul.token-filter")
public class TokenConfigurationBean {

    public List<String> getNoAuthenticationRoutes() {
        return noAuthenticationRoutes;
    }

    public void setNoAuthenticationRoutes(List<String> noAuthenticationRoutes) {
        this.noAuthenticationRoutes = noAuthenticationRoutes;
    }

    private List<String> noAuthenticationRoutes;
}
