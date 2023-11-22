package org.zerock.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page;
    private int size;
    private String type; // 서버 측 검색 처리를 위한 검색 조건 필드 추가
    private String keyword; // 서버 측 검색 처리를 위한 검색 키워드 필드 추가

    public PageRequestDTO() {
        this.page =1;
        this.size = 10;
    }

    // Pageable 타입의 객체 만들기
    public Pageable getPageable(Sort sort) {

        return PageRequest.of(page-1 , size, sort);
    }
}
