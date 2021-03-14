package com.jiaobuchong.webflux.controller;

import com.jiaobuchong.webflux.dao.MemberDao;
import com.jiaobuchong.webflux.domain.MemberDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/member")
public class MysqlMemberController {

    @Autowired
    private MemberDao memberDao;

    @GetMapping("/list")
    public Flux<MemberDO> list() {
        return memberDao.findAll();
    }

    @PostMapping("/create")
    public Mono<MemberDO> create(@RequestBody MemberDO memberDO) {
        return memberDao.save(memberDO);
    }

    @GetMapping("/detail/id")
    public Mono<MemberDO> findById(@PathVariable Long id) {
        return memberDao.findById(id);
    }

    @PostMapping("/delete/{id}")
    public Mono delete(@PathVariable Long id) {
        return memberDao.deleteById(id);
    }


}
