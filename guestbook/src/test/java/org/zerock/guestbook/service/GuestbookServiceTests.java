package org.zerock.guestbook.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

//    @Test
//    public void testRegister() {
//
//        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
//                .title("Sample Title...")
//                .content("Sample Content....")
//                .writer("user0")
//                .build();
//        System.out.println(service.register(guestbookDTO));
//    }

    // 이 테스트는 목록 처리 테스트로 엔티티 객체들이 DTO로 변환되었는지를 검사함.
    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV:  " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println(("------------------------------------"));
        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("=======================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));

    }

    @Test
    public void testSearch() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc") // 검색 조건으르 여기다 넣으면 됨
                .keyword("한글") // 검색 키워드를 여기다 넣으면 됨
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());
        System.out.println("----------------------------------------------");
        for(GuestbookDTO guestbookDTO: resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("================================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }
}
