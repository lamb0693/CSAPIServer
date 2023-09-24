package com.example.apiserver.controller;

import com.example.apiserver.dto.MemberRegisterDTO;
import com.example.apiserver.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@Log4j2
public class HomeController {
    MemberService memberService;
    @GetMapping("/")
    public List<Integer> home(){
        return Arrays.asList(5, 7 ,4);
    }

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
