package org.zerock.board.service;

import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {

    Long register(BoardDTO dto);

    // 게시물 목록 처리 인터페이스 추가  = PageResult, PageRequest 관련 인터페이스 추가
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    // 게시물 조회 처리 인터페이스 추가
    BoardDTO get (Long bno); // 게시물의 번호를 파라미터로 받아서 처리


    // 게시글 삭제 처리 인터페이스 추가
    void removeWithReplies(Long bno); //삭제 기능

    // 게시물 수정 처리 인터페이스 추가
    void modify(BoardDTO boardDTO); // 수정 기능

    default Board dtoToEntity(BoardDTO dto) {

        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }


    //BoardService 인터페이스에 추가하는 entityToDTO
    // Member 엔티티 객체, 댓글의 수, Board 엔티티 객체를 파라미터로 전달받음
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        // BoardDTo의 객체를 생성함
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) // Long으로 리턴하기 때문에 int 변환 필수
                .build();

        return boardDTO;
    }
}
