### Inflearn Querydsl 강의 정리

* Querydsl 환경설정
* 기본 Q-Type 활용
    * Q클래스 인스턴스를 사용하는 2가지 방법
    ```java
      QMember qMember = new QMember("m"); //별칭 직접 지정
      QMember qMember = QMember.member; //기본 인스턴스 사용
    ```
    * JPQL Print 
        * spring.jpa.properties.hibernate.use_sql_comments: true
* 검색 조건 쿼리
    * 기본 검색 쿼리
        * 검색 조건은 .and() , . or() 를 메서드 체인으로 연결할 수 있다.
        > 참고: select , from 을 selectFrom 으로 합칠 수 있음
    * JPQL 이 제공하는 모든 검색 조건 제공
        ```java
          member.username.eq("member1") // username = 'member1'
          member.username.ne("member1") //username != 'member1'
          member.username.eq("member1").not() // username != 'member1'
          member.username.isNotNull() //이름이 is not null
          member.age.in(10, 20) // age in (10,20)
          member.age.notIn(10, 20) // age not in (10, 20)
          member.age.between(10,30) //between 10, 30
          member.age.goe(30) // age >= 30
          member.age.gt(30) // age > 30
          member.age.loe(30) // age <= 30
          member.age.lt(30) // age < 30
          member.username.like("member%") //like 검색
          member.username.contains("member") // like ‘%member%’ 검색
          member.username.startsWith("member") //like ‘member%’ 검색
        ```
    * And 조건을 파라미터로 처리
        * where() 에 파라미터로 검색조건을 추가하면 and 조건이 추가됨
        * 이 경우 null 무시 -> 메서드 추출 활용해서 동적 쿼리를 깔끔하게 만들 수 있음.
* 결과 조회
    * fetch() : 리스트 조회, 데이터 없으면 빈 리스트 반환
    * fetchOne() : 단 건 조회
        * 결과가 없으면 null
        * 둘 이상이면 `com.querydsl.core.NonUniqueResultException`
    * fetchFirst() : limit(1).fetchOne()
    * fetchResults() : 페이징 정보 포함, total count 쿼리 추가 실행
    * fetchCount() : count 쿼리로 변경해서 count 수 조회
* 페이징
* 집합
    * JPQL
        * select
        * COUNT(m), //회원수
        * SUM(m.age), //나이 합
        * AVG(m.age), //평균 나이
        * MAX(m.age), //최대 나이
        * MIN(m.age) //최소 나이
        * from Member m
    * JPQL이 제공하는 모든 집합 함수를 제공
    * tuple은 프로젝션과 결과반환에서 설명
    * group by
* 조인 - 기본 조인
    * 기본 조인  
    조인의 기본 문법은 첫 번째 파라미터에 조인 대상 지정, 두번째 파라미터에 별칭(alias)로 사용할 Q 타입 지정
    * join() , innerJoin() : 내부 조인(inner join)
    * leftJoin() : left 외부 조인(left outer join)
    * rightJoin() : rigth 외부 조인(rigth outer join)
    * JPQL의 on 과 성능 최적화를 위한 fetch 조인 제공 다음 on 절에서 설명
    * 세타 조인  
    연관 관계가 없는 필드로 조인
    * from 절에 여러 엔티티를 선택해서 세타 조인
    * 외부 조인 불가능 다음에 설명할 조인 on을 사용하면 외부 조인 가능
* 조인 - on절
    * ON절을 활용한 조인(JPA 2.1부터 지원)
        1. 조인 대상 필터링
            > 참고: on 절을 활용해 조인 대상을 필터링 할 때, 외부조인이 아니라 내부조인(inner join)을 사용하면,
            where 절에서 필터링 하는 것과 기능이 동일하다. 따라서 on 절을 활용한 조인 대상 필터링을 사용할 때,
            내부조인 이면 익숙한 where 절로 해결하고, 정말 외부조인이 필요한 경우에만 이 기능을 사용하자.
        2. 연관관계 없는 엔티티 외부 조인
            > 하이버네이트 5.1부터 on 을 사용해서 서로 관계가 없는 필드로 외부 조인하는 기능이 추가되었다. 물론 내
              부 조인도 가능하다.
              주의! 문법을 잘 봐야 한다. leftJoin() 부분에 일반 조인과 다르게 엔티티 하나만 들어간다.
              일반조인: leftJoin(member.team, team)
              on조인: from(member).leftJoin(team).on(xxx)

    
    