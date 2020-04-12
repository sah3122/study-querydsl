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
* 조인 - 페치 조인  
  페치 조인은 SQL 에서 제공하는 기능은 아니다. SQL조인을 활용해서 연관된 엔티티를 SQL한번에 조회하는 기능이다.
  주로 성능 최적화에 사용하는 방법
    * 사용방법  
      join(), leftJoin() 등 조인 기능 뒤에 fetchJoin() 이라고 추가하면 된다.
    
* 서브쿼리
    * `com.querydsl.jpa.JPAExpressions` 사용
    * from 절의 서브쿼리 한계
        * JPA JPQL 서브쿼리의 한계점으로 from 절의 서브쿼리(인라인 뷰)는 지원하지 않는다. 당연히 Querydsl  
        도 지원하지 않는다. 하이버네이트 구현체를 사용하면 select 절의 서브쿼리는 지원한다. Querydsl도 하이버네이트 구현체를 사용하면 select 절의 서브쿼리를 지원한다.
    * from 절의 서브쿼리 해결방안
        1. 서브쿼리를 join으로 변경한다. (가능한 상황도 있고, 불가능한 상황도 있다.)
        2. 애플리케이션에서 쿼리를 2번 분리해서 실행한다.
        3. nativeSQL을 사용한다.
* 중급문법
    * 프로젝션과 결과 반환  
        프로젝션 : select 대상 지정
        * 프로젝션 대상이 하나
            * 프로젝션 대상이 하나면 타입을 명확하게 지정할 수 있음
            * 프로젝션 대상이 둘 이상이면 튜플이나 DTO로 조회
        
        * 튜플 조회
            * 프로젝션 대상이 둘 이상일 때 사용
        * DTO 조회
            * 순수 JPA에서 DTO 조회
                * 순수 JPA에서 DTO를 조회할 때는 new 명령어를 사용해야함
                * DTO의 package이름을 다 적어줘서 지저분하다
                * 생성자 방식만 지원
            * querydsl 빈 생성(Bean population)  
            결과를 DTO 반환할 때 사용  
            다음 3가지 방법 지원
                * 프로퍼티 접근
                * 필드 직접 접근
                * 생성자 사용
            * 별칭이 다를때 
                * 프로퍼티나, 필드 접근 생성 방식에서 이름이 다를 때 해결 방안
                * ExpressionUtils.as(source,alias) : 필드나, 서브 쿼리에 별칭 적용
                * username.as("memberName") : 필드에 별칭 적용
    * @QueryProjection 활용
        * 이 방법은 컴파일러로 타입을 체크 할 수 있으므로 가장 안전한 방법이다. 다만 DTO에 QueryDSL 어노테이션을 유지해야 하는 점과 DTO까지 Q 파일을 생성해야하는 단점이 있다.
    * 동적 쿼리 - booleanBuilder 사용
        * 동적 쿼리를 해결하는 두가지 방식
            * BooleanBuilder
            * Where 다중 파라미터 사용
    * 동적 쿼리 - Where 다중 파라미터 사용
        * where 조건에 null값은 무시된다.
        * 메서드를 다른 쿼리에서도 재활용 할 수 있다.
        * 쿼리 자체의 가독성이 높아진다.
        * 조합기능
            * null 체크는 주의해서 사용할 것.        
    * 수정, 삭제 벌크 연산
        > 주의: JPQL 배치와 마찬가지로, 영속성 컨텍스트에 있는 엔티티를 무시하고 실행되기 때문에 배치 쿼리를
          실행하고 나면 영속성 컨텍스트를 초기화 하는 것이 안전하다.
    * SQL function 호출
* 순수 JPA Repository & Querydsl
    * JPAQueryFactory 스프링 빈 등록
      ```java
        @Bean
              JPAQueryFactory jpaQueryFactory(EntityManager em) {
                return new JPAQueryFactory(em);
              }
      ```
      > 참고: 동시성 문제는 걱정하지 않아도 된다. 왜냐하면 여기서 스프링이 주입해주는 엔티티 매니저는 실제 동
      작 시점에 진짜 엔티티 매니저를 찾아주는 프록시용 가짜 엔티티 매니저이다. 이 가짜 엔티티 매니저는 실제
      사용 시점에 트랜잭션 단위로 실제 엔티티 매니저(영속성 컨텍스트)를 할당해준다.
      > 더 자세한 내용은 자바 ORM 표준 JPA 책 13.1 트랜잭션 범위의 영속성 컨텍스트를 참고하자.
* 동적 쿼리와 성능 최적화 조회 
    * builder 사용
        * DTO 조회
            * QueryProjection 사용  
            QueryProjection을 사용하면 DTO가 Querydsl에 의존하게 된다.  
            이러한 의존 관계를 갖지 않으려면 Projection.bean, fields, constructor를 사용하면 된다.
    * where절 파라미터 사용
         * where 절에 파라미터 방식을 사용하면 조건 재사용 가능 (null check를 잘 해야 함)

* 사용자 정의 리포지토리
    * 사용자 정의 리포지토리 사용법
    1. 사용자 정의 인터페이스 작성
    2. 사용자 정의 인터페이스 구현
    3. 스프링 데이터 리포지토리에 사용자 정의 인터페이스 상속.
* 스프링 데이터 페이징 활용
    * 스프링 데이터 Page, Pageable을 활용
    * 전체 카운트를 한번에 조회하는 단순한 방법
        * fetchResult를 사용해서 한번에 조회 후Result와 count를 PageImpl에 담아준다.
    * 데이터 내용과 전체 카운트를 별도로 조회 하는 방법
        * fetch와 count쿼리를 두번 날려 각각의 리턴값을 PageImpl 에 담아준다.
    * Count Query 최적화
        * PagableExecutionUtils.getPage() 최적화
        * 스프링 데이터 라이브러리가 제공
            * 페이지가 시작페이지 이면서 전체 사이즈가 페이지 사이즈 보다 작을떄
            * 마지막 페이지일때 사용가
    * Pagable - sort
        * 스프링 데이터에서 제공하는 sorting기능을 그대로 사용하기엔 무리가 있다.  
        조인을 한다거나 동적 정렬 기능 등리 필요하면 스프링 데이터에서 제공하는 sorting 보다 파라미터를 받아 사용하는 방식을 권장.            
* QuerydslPredicateExecutor Interface
    * Sort, Paging 지원
    * 한계점
        * 조인 불가능
        * 클라이언트가 Querydsl에 의존한다. (Sevice layer가 Querydsl이라는 구현기술에 의존)
        * 복잡한 환경에서 사용 불가능
* Querydsl Web 지원
    * Docs : https://docs.spring.io/spring-data/jpa/docs/2.2.3.RELEASE/reference/html/#core.web.type-safe
    * 한계점
        * 단순한 조건만 가능하다.
        * 조건을 커스텀 하는 기능이 복잡, 명시적이지 않다.
        * 컨트롤러가 Querydsl에 의존하는 문제점도 존재한다.
* QueryDslRepositorySupport
    * 장점
        * 스프링 데이터가 제공하는 페이징을 Querydsl로 편리하게 변환 가능(단! Sort는 오류발생)
            * getQuerydsl().applyPagination()
        * EntityManager 제공
    * 한계
        * QueryFactory 를 제공하지 않음
        * 스프링 데이터 Sort 기능이 정상 동작하지 않음
