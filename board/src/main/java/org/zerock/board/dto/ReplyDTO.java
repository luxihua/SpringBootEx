package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyDTO {

    private Long rno; // 댓글 고유 번호
    private String text;
    private String replyer;
    private Long bno; // 게시물 번호
    private LocalDateTime regDate, modDate;
}
