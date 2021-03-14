package com.jiaobuchong.webflux.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("t_member")
@Data
public class MemberDO {

    @Id
    private Long id;
    private String name;
}
