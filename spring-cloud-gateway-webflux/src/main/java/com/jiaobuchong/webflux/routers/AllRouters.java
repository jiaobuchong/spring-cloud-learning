package com.jiaobuchong.webflux.routers;

import com.jiaobuchong.webflux.handler.MemberHandler;
import com.jiaobuchong.webflux.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AllRouters {

    public static final String ROUTER_FUNCTION_PATH_PREFIX = "/router/function/user";
    public static final String MEMBER_ROUTER_FUNCTION_PATH_PREFIX = "/router/function/member";


    @Bean
    RouterFunction<ServerResponse> userRouter(UserHandler handler) {

        return RouterFunctions.nest(
//              相当于类上的 @RequestMapping("/user")，同一前缀
                RequestPredicates.path(ROUTER_FUNCTION_PATH_PREFIX),
                // 相当于方法体里的 @GetMapping("/list")
                // 得到所有用户
                RouterFunctions.route(RequestPredicates.GET("/list"), handler::getAllUser)
                        // 创建用户
                        .andRoute(
                                RequestPredicates.POST("/create")
                                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                                handler::createUser
                        )
                        // 删除用户
                        .andRoute(
                                RequestPredicates.POST("/delete/{id}"), handler::deleteUser
                        )
        );

    }


    @Bean
    RouterFunction<ServerResponse> memberRouter(MemberHandler handler) {

        return RouterFunctions.nest(
//              相当于类上的 @RequestMapping("/user")，同一前缀
                RequestPredicates.path(MEMBER_ROUTER_FUNCTION_PATH_PREFIX),
                // 相当于方法体里的 @GetMapping("/list")
                // 得到所有用户
                RouterFunctions.route(RequestPredicates.GET("/list")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
                         , handler::listMember)
                        .andRoute(
                                RequestPredicates.GET("/detail/{id}")
                                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                                handler::getMemberById
                        )
                        // 创建用户
                        .andRoute(
                                RequestPredicates.POST("/create")
                                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                                handler::saveMember
                        )
                        // 删除用户
                        .andRoute(
                                RequestPredicates.POST("/delete/{id}"), handler::deleteMember
                        )
        );

    }

}
