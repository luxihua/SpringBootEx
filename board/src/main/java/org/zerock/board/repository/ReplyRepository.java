package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 게시물 삭제 시 따져봐야 할 조건은 먼저
    // 1. 해당 게시물의 모든 댓글 삭제하기
    // 2. 해당 게시물 삭제하기
    // ReplyRepository에서는 1번을 처리하는 기능 추가하기

    @Modifying // update, delete JPQL 실행을 위한 어노테이션
    @Query("delete from Reply r where r.board.bno =:bno ")
    void deleteByBno(Long bno); // 특정 게시물 번호(bno)로 댓글 삭제하는 기능
    // 게시물로 댓글 목록 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);

}
