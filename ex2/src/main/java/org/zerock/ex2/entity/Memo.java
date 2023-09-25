package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="t_memo")
@ToString
@Getter // Lombok 에서 끌어다씀 -> 메서드를 생성할 수 있다
@Builder // 객체를 생성할 수 있다
@AllArgsConstructor // Builder와 함께 처리해야 컴파일 에러가 나지 않는다
@NoArgsConstructor // Builder와 함께 처리해야 컴파일 에러가 나지 않는다
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;

}
