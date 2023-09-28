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
