package org.zerock.guestbook.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service; // final로 선언

    @GetMapping("/")
    public String index() {

        return "redirect:/guestbook/list";
    }

    //Model을 이용해서 GuestbookServiceImpl에서 반환하는 PageResultDTO를 result이라는 이름으로 반환
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list................." + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));

    }


    // 등록 처리 컨트롤러 등록

    // Get에서는 화면 보여주기
    @GetMapping("/register")
    public void register() {
        log.info("register get...");
    }


    // Post 에서는 처리 후에 목록 페이지 이동하도록 설계
    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {

        log.info("dto..." + dto);

        // 새로 추가된 엔티티의 번호
        Long gno = service.register(dto);

        // msg 변수를 addFlashAttribute() 메서드를 이용하여 데이터를 전달함
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }


}
