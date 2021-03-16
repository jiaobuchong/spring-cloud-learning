package com.jiaobuchong.gateway.controller;

import com.google.common.collect.ImmutableMap;
import io.micrometer.core.instrument.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GatewayDynamicRouteController {

    @Autowired
    private GatewayRouteService gatewayRouteService;

    /**
     * 创建路由
     *
     * @param model
     * @return
     */
    @PostMapping("/routes")
    public Mono<ResponseEntity<Map>> create(@RequestBody Mono<GatewayRoute> model) {
        return model.flatMap(r -> {
            String routeId = r.getRouteId();
            return gatewayRouteService.findOneByRouteId(routeId)
                    .defaultIfEmpty(new GatewayRoute())
                    .flatMap(old -> {
                        if (old.getId() != null) {
                            return Mono.defer(() ->
                                    Mono.just(ResponseEntity
                                            .status(HttpStatus.FORBIDDEN)
                                            .body(
                                                    buildRetBody(403,
                                                            "routeId " + routeId + " 已存在",
                                                            null))));
                        }
//                        log.info("[ROUTE] <biz> creating. {}", Mono.defer(() -> JsonUtils.toJSON(r)));
                        return gatewayRouteService.insert(Mono.just(r))
                                .flatMap(id -> {
                                    return Mono.just((ResponseEntity.created(URI.create("/routes/" + id))
                                            .body(buildRetBody(0, "success", ImmutableMap.of("id", id)))));
                                });
                    });
        });
    }

    /**
     * 修改路由
     *
     * @param id
     * @param model
     * @return
     */
    @PutMapping("/routes/{id}")
    public Mono<ResponseEntity<Map>> update(@PathVariable Long id, @RequestBody Mono<GatewayRoute> model) {
        return model.flatMap(r -> {
            String routeId = r.getRouteId();
            return gatewayRouteService.findOneById(id)
                    .flatMap(old -> {
                        if (old == null) {
                            return Mono.defer(() -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildRetBody(403, "routeId " + routeId + " 还未创建", null))));
                        }
//                        log.info("[ROUTE] <biz> updating. id:{}\n  before:{}\n  after:{}",
//                                id, defer(() -> JsonUtils.toJSON(old)), defer(() -> JsonUtils.toJSON(r)));
                        return gatewayRouteService.update(Mono.just(r))
                                .then(Mono.defer(() -> Mono.just((ResponseEntity.ok(buildRetBody(0, "success", null))))));
                    });
        });
    }

    /**
     * @param id
     * @param status 0 正常，1 删除
     * @return
     */
    @PutMapping("/routes/{id}/{status}")
    public Mono<ResponseEntity<Object>> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        if (status == null) {
            return Mono.defer(() -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
        }
        return gatewayRouteService.updateStatus(id, status)
                .then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build())))
                .onErrorResume(t -> t instanceof NotFoundException, t -> Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * 获取单个路由信息
     *
     * @param id
     * @return
     */
    @GetMapping("/routes/{id}")
    public Mono<ResponseEntity<GatewayRoute>> route(@PathVariable Long id) {
        return gatewayRouteService.findOneById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * 刷新路由
     *
     * @return
     */
    @PostMapping("/routes/refresh")
    public Mono<ResponseEntity<Object>> refresh() {
        return gatewayRouteService.refresh()
                .map(aLong -> {
                    if (aLong > 0) {
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
                    }
                });
    }

    private Map<String, Object> buildRetBody(int code, String msg, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", msg);
        map.put("data", data);
        return map;
    }


}
