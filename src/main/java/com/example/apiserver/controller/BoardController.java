package com.example.apiserver.controller;

import com.example.apiserver.dto.BoardCreateDTO;
import com.example.apiserver.dto.MemberAuthDTO;
import com.example.apiserver.dto.MemberRegisterDTO;
import com.example.apiserver.entity.BoardEntity;
import com.example.apiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/board/")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping(value = "/create", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> create(@RequestParam String content, @RequestParam String message, @RequestParam MultipartFile file, @AuthenticationPrincipal MemberAuthDTO memberAuthDTO){
        log.info("##### create@BoardController memberAuthDTO {}", memberAuthDTO);
        String strMessage = message.replaceAll("\"", "");
        String strContent = content.replaceAll("\"", "");
        BoardCreateDTO boardCreateDTO = new BoardCreateDTO();
        boardCreateDTO.setContent(strContent);
        boardCreateDTO.setMessage(strMessage);
        if(file!=null){
            log.info(file.getOriginalFilename());
            boardCreateDTO.setFile(file);
        }
        log.info("##### create@BoardController boardCreateDTO {}", boardCreateDTO);

        try{
            boardCreateDTO.setTel(memberAuthDTO.getTel());
            BoardEntity boardEntity = boardService.create(boardCreateDTO);
            boardEntity.setUploader(null);
            return ResponseEntity.ok().body(boardEntity.toString());  //  무슨 일인지 retrofit에서  error 발생
            //return ResponseEntity.ok().body("Hello World");
        }catch(Exception e){
            log.error("Error in save member {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
