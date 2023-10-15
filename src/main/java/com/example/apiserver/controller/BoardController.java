package com.example.apiserver.controller;

import com.example.apiserver.dto.BoardCreateDTO;
import com.example.apiserver.dto.BoardListDTO;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/board/")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping(value = "/create", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> create(@RequestParam String customerTel, @RequestParam String content, @RequestParam String message, @RequestParam(required = false) MultipartFile file, @AuthenticationPrincipal MemberAuthDTO memberAuthDTO){
        log.info("##### create@BoardController memberAuthDTO {}", memberAuthDTO);
        String strCustomorTel = customerTel.replaceAll("\"", "");
        String strMessage = message.replaceAll("\"", "");
        String strContent = content.replaceAll("\"", "");
        BoardCreateDTO boardCreateDTO = new BoardCreateDTO();
        boardCreateDTO.setCustomerTel(strCustomorTel);
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
            boardEntity.setCustomer(null);
            return ResponseEntity.ok().body(boardEntity.toString());
        }catch(Exception e){
            log.error("Error in save member {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<BoardListDTO>> list(@RequestParam int noOfDisplay, @RequestParam String tel){
        log.info("##### list@BoardController noOfDisplay tel {},{}", noOfDisplay, tel);
        List<BoardListDTO> boardListDTOs;

        try{
            boardListDTOs = boardService.list(noOfDisplay, tel);
            return ResponseEntity.ok().body(boardListDTOs);
        }catch(Exception e){
            log.error("##### list@BoardController >> error in get list : {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = "/listUnReplied", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<BoardListDTO>> listUnreplied(@RequestParam int noOfDisplay){
        log.info("##### listUuReplied@BoardController noOfDisplay tel {}", noOfDisplay);
        List<BoardListDTO> boardListDTOs;

        try{
            boardListDTOs = boardService.listUnreplied(noOfDisplay);
            return ResponseEntity.ok().body(boardListDTOs);
        }catch(Exception e){
            log.error("##### listUnreplied@BoardController >> error in get list : {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
