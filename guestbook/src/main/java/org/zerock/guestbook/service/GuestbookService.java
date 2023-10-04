
package org.zerock.guestbook.service;

import org.springframework.data.domain.PageRequest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

// GuestbookDTO를 이용해 필요한 내용을 전달받고, 반환하도록 처리하는데 필요한 인터페이스
public interface GuestbookService {
    Long register(GuestbookDTO dto);

    // 서비스 계층
    // PageRequestDTO를 파라미터로, PageResultDTo를 리턴 타입으로 사용하는 getList() 설계
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    // default 기능을 활용해 dto를 jpa처리를 위한 entity로 변환할 수 있음
    // 이 default 기능을 사용하면 기존 '인터페이스 -> 추상클래스 -> 구현클래스'의 과정을 생략하고,
    // 바로 실제 코드를 인터페이스에 선언할 수 있음.
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }


    // 엔티티 객체 -> DTO로 변환
    default GuestbookDTO entityToDto(Guestbook entity) {

        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }
}
