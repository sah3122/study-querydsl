package me.study.querydsl.repository;

import me.study.querydsl.dto.MemberSearchCondition;
import me.study.querydsl.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> search(MemberSearchCondition condition);
    Page<Member> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
    Page<Member> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
}
