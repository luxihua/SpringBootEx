package org.zerock.board.repository.search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.board.entity.Board;

public interface SearchBoardRepository {

    // Board 타입 객체를 반환하는 메서드 선언
   Board search1();

   Page<Object[]> searchPage(String type, String keyword, Pageable pageable);


}
