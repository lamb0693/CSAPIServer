package com.example.apiserver.controller;

import com.example.apiserver.dto.BoardCreateDTO;
import com.example.apiserver.dto.MemberRegisterDTO;
import com.example.apiserver.entity.BoardEntity;
import com.example.apiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/board/")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> create(@ModelAttribute BoardCreateDTO boardCreateDTO){
        log.info("##### create@BoardController boardCreateDTO {}", boardCreateDTO);

        try{
            BoardEntity boardEntity = boardService.create(boardCreateDTO);
            log.info(boardCreateDTO.getFile().getOriginalFilename());
            boardEntity.setUploader(null);
            return ResponseEntity.ok().body(boardEntity.toString());
        }catch(Exception e){
            log.error("Error in save member {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
