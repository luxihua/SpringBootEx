
package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.entity.Guestbook;

// GuestbookDTO를 이용해 필요한 내용을 전달받고, 반환하도록 처리하는데 필요한 인터페이스
public interface GuestbookService {
    Long register(GuestbookDTO dto);

    // default 기능을 활용해 dto를 jpa처리를 위한 entity로 변환할 수 있음
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }
}
