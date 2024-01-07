package org.zerock.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.entity.Note;
import org.zerock.club.service.Noteservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {

    private final Noteservice noteservice; //final

    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO){

        log.info("-----------register-------------------------------");
        log.info(noteDTO);

        Long num = noteservice.register(noteDTO);

        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    // 특정 번호의 게시물을 조회하는 기능
    @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num) {

        log.info("--------------------read-----------------------");
        log.info(num);
        return new ResponseEntity<>(noteservice.get(num), HttpStatus.OK);
    }

    // 특정 이메일을 가진 회원이 작성한 모든 Note를 조회할 수 있는 기능
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDTO>> getList(String email) {

        log.info("--------------------getList------------------------");
        log.info(email);

        return new ResponseEntity<>(noteservice.getAllWithWriter(email), HttpStatus.OK);
    }

    // 게시물 삭제 기능
    @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE) // json 형식이 아닌 단순한 문자열로 지정
    public ResponseEntity<String> remove(@PathVariable("num") Long num) {

        log.info("---------------------remove-----------------------------");
        log.info(num);

        noteservice.remove(num);

        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    // 게시물 수정 기능
    @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO) { // JSON 데이터에 num 속성을 포함하여 전송함.

        log.info("---------------------modify------------------------------");
        log.info(noteDTO);

        noteservice.modify(noteDTO);

        return new ResponseEntity<>("modified", HttpStatus.OK);
    }

}