package com.jiaobuchong.gateway.controller;

import reactor.core.publisher.Mono;

public class GatewayRouteService {
    public Mono<GatewayRoute> findOneByRouteId(String routeId) {
        return Mono.just(new GatewayRoute());
    }

    public Mono<Object> insert(Mono<GatewayRoute> just) {
        return Mono.just(null);
    }

    public Mono<GatewayRoute> findOneById(Long id) {
        return Mono.just(null);
    }

    public <T> Mono<T> update(Mono<GatewayRoute> just) {
        return Mono.just(null);
    }

    public <T> Mono<T> updateStatus(Long id, Integer status) {
        return Mono.just(null);
    }

    public Mono<Long> refresh() {
        return Mono.just(null);
    }
}
