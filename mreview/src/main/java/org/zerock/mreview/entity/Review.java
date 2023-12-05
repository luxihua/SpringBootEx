package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
// Movie와 member를 참조하고 있음
@ToString(exclude = {"movie", "member"})
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    // ManyToOne -> 양쪽을 참조하는 구조
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int grade;
    private String text;


    //평점 리뷰를 수정하는 기능 추가
    public void changeGrade(int grade) {
        this.grade = grade;
    }

    // 텍스트 리뷰를 수정하는 기능 추가
    public void changeText(String text) {
        this.text = text;
    }

}
