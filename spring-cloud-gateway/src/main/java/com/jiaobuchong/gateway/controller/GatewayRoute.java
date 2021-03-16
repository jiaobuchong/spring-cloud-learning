package com.jiaobuchong.gateway.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayRoute {
    private String routeId;
    private String uri;
    private Integer order;
    private List<GatewayPredicateDefinition> predicates;
    private List<GatewayFilterDefinition> filters;
    private Long id;
}
