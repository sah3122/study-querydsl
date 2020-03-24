package me.study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.study.querydsl.dto.MemberSearchCondition;
import me.study.querydsl.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static me.study.querydsl.entity.QMember.member;
import static me.study.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> search(MemberSearchCondition condition) {
        return queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()),
                        ageBetween(condition.getAgeLoe(), condition.getAgeLoe())
                )
                .fetch();
    }

    @Override
    public Page<Member> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()),
                        ageBetween(condition.getAgeLoe(), condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();// contents용과 count용 쿼리를 두번 날리게 된다. 페이징 기능에선 이걸 쓸것
        List<Member> content = results.getResults(); // 실제 쿼리 내역
        long total = results.getTotal(); // 전체 수

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Member> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<Member> members = queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()),
                        ageBetween(condition.getAgeLoe(), condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()),
                        ageBetween(condition.getAgeLoe(), condition.getAgeLoe())
                )
                .fetchCount();

        return new PageImpl<>(members, pageable, total); // content 쿼리랑 실제 쿼리랑 분리를 해서 카운트 쿼리를 최적화 할 수 있다.
    }

    private BooleanExpression ageBetween(int ageLoe, int ageGoe) {
        return ageGoe(ageLoe).and(ageGoe(ageGoe));
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }
}
