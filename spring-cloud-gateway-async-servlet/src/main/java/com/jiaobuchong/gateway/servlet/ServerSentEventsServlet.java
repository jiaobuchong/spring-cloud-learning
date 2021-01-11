package com.jiaobuchong.gateway.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@WebServlet("/sse")
public class ServerSentEventsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("utf-8");
//        使用场景可以是服务器往前台推送数据
        for (int i = 0; i < 5; i++) {
            // 指定事件的标志
            response.getWriter().write("event:me\n");
            response.getWriter().write("data: " + i + "\n\n");
            response.getWriter().flush();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
