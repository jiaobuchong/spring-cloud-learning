package com.jiaobuchong.webflux.client.core.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 服务器信息
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {

    // 服务器 url
    private String url;



}
