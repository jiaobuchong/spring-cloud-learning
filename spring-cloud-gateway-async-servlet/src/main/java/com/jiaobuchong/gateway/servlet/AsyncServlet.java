package com.jiaobuchong.gateway.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

// 异步 servlet
@WebServlet(asyncSupported = true, urlPatterns = "/AsyncServlet")
public class AsyncServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        // 开启异步
        AsyncContext asyncContext = req.startAsync();
        // 耗时的操作放在一个异步操作里
        CompletableFuture.runAsync(() -> {
            doSomething(asyncContext, asyncContext.getRequest(), asyncContext.getResponse());
        });
        long end = System.currentTimeMillis();
        System.out.println("a sync cost: " + (end - start));
    }

    private void doSomething(AsyncContext asyncContext, ServletRequest req, ServletResponse resp) {
        // 模拟耗时操作
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            resp.getWriter().append("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 业务代码处理完毕，通知结束
        asyncContext.complete();
    }
}
