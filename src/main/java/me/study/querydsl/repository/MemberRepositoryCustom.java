package me.study.querydsl.repository;

import me.study.querydsl.dto.MemberSearchCondition;
import me.study.querydsl.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> search(MemberSearchCondition condition);
}
