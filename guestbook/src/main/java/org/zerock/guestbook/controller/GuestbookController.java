package org.zerock.guestbook.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        // modal창을 띄우겠다는 뜻
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    //@GetMapping("/read"와 "/modify" 같이 처리)
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno: " +gno);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }

    // 삭제의 경우는 Post 방식으로 gno 값을 전달후
    // 다시 목록의 첫 페이지로 이동하는 방식
    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes) {
        log.info("gno:" + gno);

        service.remove(gno);

        // post 보낸 후 modal창 띄우기
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    // 수정의 경우도 Post 방식으로 데이터 처리
    @PostMapping("/modify")
    // modify 메서드에는 3개의 파라미터가 들어감
    // 1. 수정해야 하는 글의 정보를 가진 GuestbookDTO
    // 2. 기존의 페이지 정보 유지를 위한 PageRequestDTO
    // 3. 리다이렉트를 위한 RedirectAttributes
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes) {
        log.info("post modify...............................");
        log.info("dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());


        return "redirect:/guestbook/read";
    }

}
