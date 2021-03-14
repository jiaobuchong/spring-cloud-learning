package com.jiaobuchong.webflux.dao;

import com.jiaobuchong.webflux.domain.MemberDO;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MemberDao extends ReactiveCrudRepository<MemberDO, Long> {
}
