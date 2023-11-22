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

    // 다대일 관계 지정
    @ManyToOne(fetch = FetchType.LAZY)  // fetch 모드를 lazy로 지정(Lazy = 지연 로딩)
    private Member writer; // 연관관계 지정
    // 이렇게 fetch를 지정하면 데이터베이스와의 연결이 끊어진 이후 데이터를 조회하는 것이기 때문에 오류가 난다.
    // 해결법은 BoardRepositoryTest에서 @Transactional 애노테이션을 추가하면 됨.

    //////////// 이제부터 게시글 수정하기////////////
    // 게시물 제목 수정 메소드
    public void changeTitle(String title) {
        this.title = title;
    }

    // 게시글 내용 수정 메서드
    public void changeContent(String content) {
        this.content = content;
    }

}
