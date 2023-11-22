package org.zerock.board.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository; // 게시글 저장소 자동 주입
    private final ReplyRepository replyRepository; // 댓글 저장소 자동 주입

    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);

        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }


    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0], (Member)en[1], (Long)en[2]));
//
//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
                return new PageResultDTO<>(result, fn);
    }

    // 게시물 조회 인터페이스 구현
    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[])result;

        // Board 엔티티와 Member 엔티티, 댓글의 수(Long)을 가져옴
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }


    // 삭제 기능 구현
    @Transactional // 필요할 때 마다 sql과 연결 가능한 애노테이션
    @Override
    public void removeWithReplies(Long bno) {

        // 댓글 부터 삭제
        replyRepository.deleteByBno(bno);

        // 게시물 삭제(id 라는 기본키를 참조하여 삭제하기에 deleteByID)
        repository.deleteById(bno);

    }

    // 수정 기능 구현
    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = repository.getReferenceById(boardDTO.getBno());

        if(board != null) {

            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            repository.save(board); // 수정된 board 테이블 저장하기
        }
    }



}
