package me.study.querydsl.repository;

import me.study.querydsl.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void basicTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all).containsExactly(member);

        List<Member> member1 = memberJpaRepository.findByUsername("member1");
        assertThat(member1).containsExactly(member);
    }

    @Test
    void baisQuerydslTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> all = memberJpaRepository.findALL_Querydsl();
        assertThat(all).containsExactly(member);

        List<Member> member1 = memberJpaRepository.findByUsername_Querydsl("member1");
        assertThat(member1).containsExactly(member);
    }
}