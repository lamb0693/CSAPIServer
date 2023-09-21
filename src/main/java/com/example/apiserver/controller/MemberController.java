package com.example.apiserver.controller;

import com.example.apiserver.dto.MemberRegisterDTO;
import com.example.apiserver.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

@Controller
@RequestMapping("/member")
@Log4j2
@AllArgsConstructor
public class MemberController {
    MemberService memberService;
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody MemberRegisterDTO memberRegisterDTO){
        try{
            MemberRegisterDTO returnDTO = memberService.register(memberRegisterDTO);
            return ResponseEntity.ok().body(returnDTO.toString());
        }catch(Exception e){
            log.error("Error in save member {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
