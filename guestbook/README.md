# Spring MVC/JPA/Thymeleaf 연습

----------------------------------
## 프로젝트 기본 구조


#### 계층별 구분

1. 브라우저에서 들어오는 Request는 GuestbookController 객체로 처리한다.
2. GuestbookController는 GuestbookService 타입을 주입받는 구조로 만들고, 이를 이용해서 원하는 작업을 처리한다.
3. GuestbookRepository는 SpringDataJPA를 이용하여 구성하고, GuestbookServiceImpl 클래스에 주입하여 사용한다.
4. 마지막 결과는 Thymeleaf을 이용하여 레이아웃 템플릿을 활용해서 처리한다.


위의 구조를 처리하는데 있어서 각 계층 사이에는 데이터를 주고받는 용도의 클래스(DTO객체, Entity 객체)를 활용하게 된다.


#### DTO와 엔티티 객체의 역할

5. 브라우저에서 전달되는 Request는 GuestbookController에서 DTO의 형태로 처리된다.
6. GuestbookRepository는 엔티티 타입을 이용하므로 중간에 Service 계층에서는 DTO와 엔티티의 변환을 처리한다.


JPA를 이용하는 경우 엔티티 객체는 항상 JPA가 관리하는 콘텍스트에 속해 있기 때문에 가능하면 JPA 영역을 벗어나지 않도록 작성


------------------------------
## 자동으로 처리되는 날짜/시간 설정


데이터의 등록 시간과 수정 시간과 같이 자동으로 추가되고 변경되어야 하는 칼럼
-> 자동처리 애노테이션을 활용하면 됨

    entity 패키지 생성 후 추상 클래스로 작성
    
BaseEntity.java
```Java
package org.zerock.guestbook.entity;


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

BaseEntity 클래스에서 테이블이 생성되는게 아니라, 이 클래스를 상속한 엔티티의 클래스로 DB 테이블이 생성된다.

#### JPA 내부의 엔티티 객체 동작 방식

1. AuditingEntityListener -> JPA 내부에서 엔티티 객체가 생성/변경되는 감지하여 regDate, modDate에 적절한 값 지정
  - AuditingEntityListener 활성화를 위해선 @EnableJpaAuditing 설정 추가 필요
2. @CreatedData -> JPA에서 엔티티의 생성 시간을 처리
3. @LastModifiedDate -> 최종 수정 시간을 자동으로 처리
    
@EnableJpaAuditing 설정 추가 클래스 작성
GuestbookApplication
```Java
package org.zerock.guestbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // AuditingEntityListener 활성화를 위한 애노테이션 추가 설정
public class GuestbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuestbookApplication.class, args);
    }

}
```

-------------------------------
#### BaseEntity.java을 상속한 Guestbok.java 클래스 작성

Guestbook.java
``` Java
package org.zerock.guestbook.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook  extends BaseEntity{


    // 사실상 변수는 4개 선언했지만, BaseEntity 추상클래스를 상속했기 때문에,
    // 빌드해보면 칼럼이 총 6개가 되어있음
    // regDate. modDate가 칼럼으로 추가됨
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
```


------------------------
#### Querydsl을 이용하여 기본적인 CRUD 사용하기

1. GuestbookReposiroty 인터페이스에 QuerydslPredicateExecutor 인터페이스 추가 상속

``` Java
package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>,
        QuerydslPredicateExecutor<Guestbook>  {
}

```

2. GestbookReposirotyTests.java 클래스에 300개 데이터 넣기

``` Java

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1, 300).forEach(i -> {

            Guestbook guestbook = Guestbook.builder()
                    .title("Title...." + i)
                    .content("Content....." + i)
                    .writer("user" + (i%10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook)); // save을 해야만 entity수정 후 반영이 됨

        });
    }
```

--------------------------------
3. 항목 검색법

  - Guestbook에서 단일 항목 검색하는 법
    - 제목에 '1'이라는 글자가 있는 엔티티를 검색하기 위한 class 작성법
  ``` Java
  @Test
  public void testQuery1() {

      Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

      QGuestbook qGuestbook = QGuestbook.guestbook; // 1.동적처리를 위해 Q도메인 클래스를 가져옴
      /////////////////////////////////////////////////-> 이후 title, content 와 같은 필드를 변수를 활용할 수 있음

      String keyword = "1";

      BooleanBuilder builder = new BooleanBuilder(); // 2.BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너

      BooleanExpression expression = qGuestbook.title.contains(keyword); // 3. 필드와 함께 결합하여 조건 생성

      builder.and(expression); // 4. 만드러진 조건은 where문에 or, and와 같은 키워드와 결합시킴

      // 5. BooleanBuilder는 GuestbookRepository에 추가된 findAll 메서드를 사용할 수 있음
      Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

      result.stream().forEach(guestbook -> {
          System.out.println(guestbook);
      });

  }
```
   - Guestbook에서 다중 항목 검색하는법
    - 제목 혹은 내용에 특정한 키워드가 있고, gno가 0보다 크다
    
``` Java
@Test
   public void testQuery2() {
       Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

       QGuestbook qGuestbook = QGuestbook.guestbook;

       String keyword = "1";

       BooleanBuilder builder = new BooleanBuilder(); // 한마디로 where조건 절을 위한 컨테이너다!!

       BooleanExpression exTitle = qGuestbook.title.contains(keyword);

       BooleanExpression exContent = qGuestbook.content.contains(keyword);

       BooleanExpression exAll = exTitle.or(exContent); // 1. 제목 혹은 내용으로 결합

       builder.and(exAll); // 1번 내용을 컨테이너에 결합

       builder.and(qGuestbook.gno.gt(0L)); // gno가 0보다 크다는 조건을 2번과 and로 컨테이너에 연결함

       // 제목 혹은 내용에 특정한 키워드가 있고! gno가 0보다 크다는 조건에 맞는 데이터 = result
       Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

       result.stream().forEach(guestbook -> {
           System.out.println(guestbook);
       });
   }
```

