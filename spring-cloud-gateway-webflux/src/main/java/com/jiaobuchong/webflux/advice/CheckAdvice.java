package com.jiaobuchong.webflux.advice;

import com.jiaobuchong.webflux.exception.CheckException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * 异常处理切面
 */
@ControllerAdvice
public class CheckAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleBingException(WebExchangeBindException e) {

        return new ResponseEntity<>(getMessage(e), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(CheckException.class)
    public ResponseEntity<String> handleCheckException(CheckException e) {
        return new ResponseEntity<>(getMessage(e), HttpStatus.BAD_REQUEST);

    }

    private String getMessage(CheckException e) {
        return e.getFieldName() + ",error value:" + e.getFieldValue();
    }

    // 把校验异常转换成 String
    private String getMessage(WebExchangeBindException ex) {
        return ex.getFieldErrors().stream().map(e -> e.getField() + ":"  + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }

}
