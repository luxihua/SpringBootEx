package org.zerock.mreview.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// MovieDTO에는 업로드 된 파일들의 정보를 포함해야 함
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Long mno;

    private String title;


    // 영화 이미지들도 수집하여 전달해야 하므로 내부 인스턴스 필요
    @Builder.Default
    private List<MovieImageDTO> imageDTOList = new ArrayList<>();

    // 영화의 평균 평점
    private double avg;

    // 리뷰 수 jpa의 count()
    private int reviewCnt;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
