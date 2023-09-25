package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    // between 과 orderby가 포함된 쿼리메서드 추가를 위한 인터페이스
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
    // between과 정렬이 좀 더 간편한 메서드 사용을 위한 인터페이스 추가
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // 삭제와 관련된 인터페이스 추가
    void deleteMemoByMnoLessThan(Long num);

}
