package com.study.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;


@Component
public class SendErrorRestFilter extends SendErrorFilter {
    protected  static final Logger logger = LoggerFactory.getLogger(SendErrorRestFilter.class);

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        Throwable throwable = findCauseException(context.getThrowable());
        //获取到状态码
        String status = String.valueOf(context.getResponseStatusCode());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","异常码"+status);
        jsonObject.put("errorMessage",throwable.getMessage());

        //记录日志
        logger.warn("有个异常:",context.getThrowable());

        context.setResponseBody(jsonObject.toJSONString());
        context.getResponse().setContentType("text/html;charset=UTF-8");
        // 清空异常
        context.remove("throwable");

        return null;
    }

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    // 找出最初始的异常
    Throwable findCauseException(Throwable throwable) {
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return throwable;
    }
}
