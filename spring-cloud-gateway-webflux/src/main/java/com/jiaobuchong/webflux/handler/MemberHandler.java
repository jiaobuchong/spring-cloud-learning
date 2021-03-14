package com.jiaobuchong.webflux.handler;

import com.jiaobuchong.webflux.dao.MemberDao;
import com.jiaobuchong.webflux.domain.CommonResponse;
import com.jiaobuchong.webflux.domain.MemberDO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MemberHandler {

    @Autowired
    private MemberDao memberDao;

    @NotNull
    public Mono<ServerResponse> saveMember(ServerRequest serverRequest) {
        Mono<MemberDO> memberDOMono = serverRequest.bodyToMono(MemberDO.class);
        MemberDO memberDO = memberDOMono.block();
        return ServerResponse.ok().build(memberDao.save(memberDO).then());
    }


    @NotNull
    public Mono<ServerResponse> deleteMember(ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return ServerResponse.ok().build(memberDao.deleteById(id).then());
    }

    public Mono<ServerResponse> getMemberById1 (ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        Mono<MemberDO> mono = memberDao.findById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(mono, MemberDO.class);
    }

    @NotNull
    public Mono<ServerResponse> getMemberById (ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        Mono<MemberDO> mono = memberDao.findById(id);
        Mono<CommonResponse<MemberDO>> commonResponseMono = mono.map(memberDO -> {
            CommonResponse<MemberDO> commonResponse = new CommonResponse<>();
            commonResponse.setData(memberDO);
            return commonResponse;
        });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(commonResponseMono, CommonResponse.class);
    }

    @NotNull
    public Mono<ServerResponse> listMember (ServerRequest serverRequest) {
        Flux<MemberDO> memberDOFlux = memberDao.findAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(memberDOFlux, MemberDO.class);
    }

}
