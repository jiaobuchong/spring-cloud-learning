package com.jiaobuchong.webflux.client.core;

import com.jiaobuchong.webflux.client.core.bean.MyMethodInfo;
import com.jiaobuchong.webflux.client.core.bean.ServerInfo;
import com.jiaobuchong.webflux.client.core.interfaces.ProxyCreator;
import com.jiaobuchong.webflux.client.core.interfaces.RestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// 使用jdk动态代理实现代理类
@Slf4j
public class JdkProxyCreator implements ProxyCreator {

    @Override
    public Object createProxy(Class<?> type) {
        log.info("createProxy: " + type);
        // 根据接口得到 api 服务器信息
        ServerInfo serverInfo = extractServerInfo(type);
        // 给每个代理类一个实现
        RestHandler restHandler = new WebClientRestHandler();
        log.info("serverInfo: " + serverInfo);

        // 初始化服务器信息，初始化 webClient
        restHandler.init(serverInfo);

        return Proxy.newProxyInstance(
                // 类加载器
                this.getClass().getClassLoader(),
                // 代理的服务器类型
                new Class[]{type},
                // 代理方法执行的地方
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 根据方法和参数得到调用信息
                        MyMethodInfo methodInfo = extractMethodInfo(method, args);
                        log.info("method info: " + methodInfo);
                        // 调用 rest
                        return restHandler.invokeRest(methodInfo);
                    }

                    // 根据方法定义和调用参数得到调用的相关信息
                    private MyMethodInfo extractMethodInfo(Method method, Object[] args) {
                        MyMethodInfo methodInfo = new MyMethodInfo();
                        extractUrlAndMethod(method, methodInfo);
                        extractRequestParamAndBody(method, args, methodInfo);

                        return methodInfo;
                    }

                    //  得到请求的参数和 body
                    private void extractRequestParamAndBody(Method method, Object[] args, MyMethodInfo methodInfo) {
                        // 得到调用参数和 body
                        // 获取参数
                        Parameter[] parameters = method.getParameters();

                        // 参数名称和参数值的 map
                        Map<String, Object> params = new LinkedHashMap<>();
                        methodInfo.setParams(params);

                        for (int i = 0; i < parameters.length; i++) {
                            // 是否带 PathVariable
                            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
                            if (pathVariable != null) {
                                params.put(pathVariable.value(), args[i]);
                            }

                            // 是否带 RequestBody
                            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
                            if (requestBody != null) {
                                methodInfo.setBody((Mono<?>) args[i]);
                            }
                        }
                    }

                    // 得到请求的 url 和方法
                    private void extractUrlAndMethod(Method method, MyMethodInfo methodInfo) {
                        // 得到请求的 url 和请求方法
                        Annotation[] annotations = method.getAnnotations();
                        for (Annotation annotation : annotations) {
                            if (annotation instanceof GetMapping) {
                                GetMapping getMapping = (GetMapping) annotation;
                                methodInfo.setUrl(getMapping.value()[0]);
                                methodInfo.setMethod(HttpMethod.GET);
                            } else if (annotation instanceof PostMapping) {
                                PostMapping postMapping = (PostMapping) annotation;
                                methodInfo.setUrl(postMapping.value()[0]);
                                methodInfo.setMethod(HttpMethod.POST);
                            }
                        }
                    }
                });
    }

    // 提取服务器信息
    private ServerInfo extractServerInfo(Class<?> type) {
        ServerInfo serverInfo = new ServerInfo();
        ApiServer apiServer = type.getAnnotation(ApiServer.class);
        serverInfo.setUrl(apiServer.value());
        return serverInfo;
    }
}
