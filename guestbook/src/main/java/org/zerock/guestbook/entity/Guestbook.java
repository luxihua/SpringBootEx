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
