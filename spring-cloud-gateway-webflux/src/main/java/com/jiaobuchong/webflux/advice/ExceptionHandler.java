package com.jiaobuchong.webflux.advice;

import com.jiaobuchong.webflux.exception.CheckException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)   //设置异常处理器优先级，数值越小优先执行
public class ExceptionHandler implements WebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 设置响应头
        ServerHttpResponse serverResponse = exchange.getResponse();
        // 设置响应状态
        serverResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        // 设置返回类型
        serverResponse.getHeaders().setContentType(MediaType.TEXT_PLAIN);

        // 异常信息
        String errorMsg = getMessage(ex);
        DataBuffer dataBuffer = serverResponse.bufferFactory().wrap(errorMsg.getBytes());
        return serverResponse.writeWith(Mono.just(dataBuffer));
    }

    private String getMessage(Throwable ex) {
        // 已知异常
        if (ex instanceof CheckException) {
            CheckException checkException = (CheckException) ex;
            return checkException.getFieldName() + ": invalid value " + checkException.getFieldValue();

        } else {
            // 未知异常
            ex.printStackTrace();
            return ex.toString();
        }
    }
}
