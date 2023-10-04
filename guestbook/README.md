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

    
    
