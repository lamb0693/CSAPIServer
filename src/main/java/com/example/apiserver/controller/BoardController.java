package com.example.apiserver.controller;

import com.example.apiserver.dto.*;
import com.example.apiserver.entity.BoardEntity;
import com.example.apiserver.service.BoardService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        try{
            boardCreateDTO.setTel(memberAuthDTO.getTel());
            log.info("##### create@BoardController boardCreateDTO {}", boardCreateDTO);
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


    @GetMapping(value = "/listUnReplied/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PagedBoardListDTO> listUnreplied(@PathVariable("page") String page){
        if(page == null) page= "0";
        PagedBoardListDTO pagedBoardListDTO = null;

        try{
            int nPage = Integer.parseInt(page);
            log.info("##### listUuReplied@BoardController page {}", nPage);
            pagedBoardListDTO = boardService.listUnreplied(nPage);
            return ResponseEntity.ok().body(pagedBoardListDTO);
        }catch(Exception e){
            log.error("##### listUnreplied@BoardController >> error in get list : {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value="/markReply")
    @ResponseBody
    public ResponseEntity<String> markReplied(@RequestParam String customerTel){
        String strCustomorTel = customerTel.replaceAll("\"", "");
        log.info("markReplied customorTel : {}", strCustomorTel);

        try{
            boardService.markReplied(strCustomorTel) ;
            return ResponseEntity.ok().body("success");
        } catch(Exception e){
            log.error("markReplied : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value="/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam Long id){

        try {
            String filepath = boardService.getFilePath(id);
            Path path = Paths.get(filepath);
            byte[] fileContent = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + path.getFileName());
            headers.add("Content-Type", Files.probeContentType(path));

            return ResponseEntity.ok().headers(headers).body(fileContent);
        } catch (Exception e) {
            log.error("download : {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

    }

}
