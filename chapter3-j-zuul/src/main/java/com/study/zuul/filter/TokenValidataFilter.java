package com.study.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.study.zuul.Configuration.TokenConfigurationBean;
import com.study.zuul.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenValidataFilter extends ZuulFilter {

    @Autowired
    TokenConfigurationBean tokenConfigurationBean;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public boolean shouldFilter() {
        //过滤我们的url，如果说是相应请求比如登录:不做任何校验直接放行
        RequestContext ctx = RequestContext.getCurrentContext();
        return !tokenConfigurationBean.getNoAuthenticationRoutes().contains(ctx.get("proxy"));
    }

    @Override
    public Object run() throws ZuulException {
        //校验token
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest req = ctx.getRequest();

        String token = req.getHeader("Authorization");
        if (token == null) {
            forBidden();
            return null;
        }
        //校验token 权限码
        Claims claims = jwtTokenProvider.parseToken(token);
        if (claims == null) {
            forBidden();
            return null;
        }
        System.out.println("请求token内容是:" + JSONObject.toJSONString(claims));

        return null;
    }

    void forBidden() {
        RequestContext.getCurrentContext().setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
        ReflectionUtils.rethrowRuntimeException(new ZuulException("无访问权限", HttpStatus.SC_FORBIDDEN, "token校验不通过"));
    }
}
