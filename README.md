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