# N:1 연관관계 

-----------------------------------

## 엔티티 클래스 추가

- 필요한 클래스

    1. BaseEntity 클래스 추가
    ``` Java
    package org.zerock.board.entity;


    // 자동 날짜/시간 출력은 애노테이션을 통해 가능
    // 단, 엔티티객체로 처리해야 함으로 entity 패키지 내 추상클래스를 작성하여서 등록시간과 수정 시간을 출력할 것임.


    import lombok.Getter;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;

    import javax.persistence.Column;
    import javax.persistence.EntityListeners;
    import javax.persistence.MappedSuperclass;
    import java.time.LocalDateTime;

    @MappedSuperclass
    @EntityListeners(value = {AuditingEntityListener.class})
    @Getter
    abstract public class BaseEntity {

        @CreatedDate // 엔티티의 생성 시간을 자동 처리 애노테이션
        @Column( name = "regDate", updatable = false)
        // 등록 날짜 및 시간
        private LocalDateTime regDate;

        @LastModifiedDate // 엔티티의 최종 수정 시간 자동 처리 애노테이션
        @Column( name = "modDate")
        // 수정 날짜 및 시간
        private LocalDateTime modDate;
    }
    ```
    
    2. 연관관계 설정을 위한 클래스 추가
    
    - entity 클래스 내 Member(회원), Board(게시물), Reply(댓글) 엔티티 클래스 추가
    
    Member 클래스
    
    ``` JAVA
    package org.zerock.board.entity;

    import lombok.*;

    import javax.persistence.Entity;
    import javax.persistence.Id;

    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public class Member extends BaseEntity{

        @Id
        private String email; // 이메일 주소가 PK

        private String password;

        private String name;
    }
    
    ```
    
    
-------------------------------
    

## @ManyToOne 어노테이션 처리

- 관계 설정 : PK를 먼저 고려하여 설정

1. Board 클래스 : member쪽의 email을 board에서는 FK로 참조

    ``` Java
    package org.zerock.board.entity;


    import lombok.*;

    import javax.persistence.*;


    // Member의 이메일(PK)를 FK로 참조하는 구조
    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString(exclude = "writer") // @ToString은 항상 exclude
    public class Board extends BaseEntity  {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long bno;

        private String title;

        private String content;

        @ManyToOne // 다대일 관계 지정
        private Member writer; // 연관관계 지정

    }
    ```

 2. Reply 클래스 : Board쪽의 PK를 참조하여 구성
 
    ``` Java
    package org.zerock.board.entity;


    import lombok.*;

    import javax.persistence.*;

    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString(exclude = "board") // @ToSting 주의
    public class Reply extends BaseEntity {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long rno;

        private String text;

        private String replyer;

        @ManyToOne // Reply쪽에서는 Board 쪽의 PK를 참조하여 구성
        private Board board; // 연관관계 지정

    }
    ```

-----------------------------

## Repository 인터페이스 추가

1. MemberRepository 인터페이스

``` Java
package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.Member;

public interface MemberRepository extends JpaRepository<Member,String> {
}
```

2. BoardRepository 인터페이스

``` Java
package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.Board;



public interface BoardRepository extends JpaRepository<Board,Long> {
}
```

3. ReplyRepository 인터페이스

``` Java
package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
```


---------------------------


  
