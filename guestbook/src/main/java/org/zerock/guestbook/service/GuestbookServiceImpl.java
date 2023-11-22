package org.zerock.guestbook.service;

// GuestbookDTO를 이용해 필요한 내용을 전달받고, 반환하도록 처리하는데 필요한 클래ㅣ

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository; // 반드시 final로 선언해야함

    // 파라미터를 통해 GestbookServiced에서 가져온 dto를 전달할 수 있음
    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO-------------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }

    // GuestbookService에서의 getList()을 가져와서 PageResultDTO로 구성함
    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 처리

        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable); // Querydsl 사용

        // Page<Entity>와 Fuction을 전달하여 DTO의 리스트로 변환
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }


    // GuesetbookService에서 가져와 방명록 조회 메서드 구현
    @Override
    public GuestbookDTO read(Long gno) {

        Optional<Guestbook> result = repository.findById(gno); // findById로 GuestbookRepository에서 엔티티 객체를 가져왔다면,

        return result.isPresent()? entityToDto(result.get()): null; // null이 아니면 DTO로 변환하여 반환한다.
    }

    // GuestbookService에서 가져와 방명록 삭제 메서드 구현
    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    // GuestbookService에서 가져와 방명록 조회 메서드 구현
    @Override
    public void modify(GuestbookDTO dto) {

        // 업데이트 하는 항목은, '제목', '내용' == readonly 속성 없는 것들
        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()) {
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity); // 수정 후 저장
        }

    }

    // 검색을 위한 메서드 추가(추상클래스에서 작성한게 아니라 PageRequestDTO에서 가져와서 사용)
    private BooleanBuilder getSearch(PageRequestDTO requesetDTO) {
        //Querydsl 처리
        String type = requesetDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = requesetDTO.getKeyword();

        BooleanExpression expression = qGuestbook.gno.gt(0L); //gno >0 조건만 생성하기

        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0) {
            // 검색 조건이 없는 경우 return하기
            return booleanBuilder;
        }

        // 검색 조건 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        // 제목(t)의 검색 조건
        if(type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        // 내용(c)의 검색 조건
        if(type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }

        // 작성자(w)의 검색 조건
        if(type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
