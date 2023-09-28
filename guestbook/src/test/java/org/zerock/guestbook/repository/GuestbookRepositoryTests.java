package org.zerock.guestbook.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

//    @Test
//    public void insertDummies(){
//
//        IntStream.rangeClosed(1, 300).forEach( i -> {
//
//            Guestbook guestbook = Guestbook.builder()
//                    .title("Title...." + i)
//                    .content("Content....." + i)
//                    .writer("user" + (i%10))
//                    .build();
//            System.out.println(guestbookRepository.save(guestbook)); // save을 해야만 entity수정 후 반영이 됨
//
//        });
//    }

//
//    @Test
//    public void updateTest() {
//        Optional<Guestbook> result = guestbookRepository.findById(300L); // 존재하는 번호로 테스트
//
//        if(result.isPresent())  {
//
//            Guestbook guestbook = result.get();
//
//            guestbook.changeTitle("Changed Title....");
//            guestbook.changeContent("Changed Content....");
//
//            guestbookRepository.save(guestbook);
//        }
//    }
//
//    @Test
//    public void testQuery1() {
//
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
//
//        QGuestbook qGuestbook = QGuestbook.guestbook; // 1.동적처리를 위해 Q도메인 클래스를 가져옴
//        /////////////////////////////////////////////////-> 이후 title, content 와 같은 필드를 변수를 활용할 수 있음
//
//        String keyword = "1";
//
//        BooleanBuilder builder = new BooleanBuilder(); // 2.BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너
//
//        BooleanExpression expression = qGuestbook.title.contains(keyword); // 3. 필드와 함께 결합하여 조건 생성
//
//        builder.and(expression); // 4. 만드러진 조건은 where문에 or, and와 같은 키워드와 결합시킴
//
//        // 5. BooleanBuilder는 GuestbookRepository에 추가된 findAll 메서드를 사용할 수 있음
//        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
//
//        result.stream().forEach(guestbook -> {
//            System.out.println(guestbook);
//        });
//
//    }

    @Test
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); // 한마디로 where조건 절을 위한 컨테이너다!!

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent); // 1. 제목 혹은 내용으로 결합

        builder.and(exAll); // 1번 내용을 컨테이너에 결합

        builder.and(qGuestbook.gno.gt(0L)); // gno가 0보다 크다는 조건을 2번과 and로 컨테이너에 연결함

        // 제목 혹은 내용에 특정한 키워드가 있고! gno가 0보다 크다는 조건에 맞는 데이터 = result
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}

