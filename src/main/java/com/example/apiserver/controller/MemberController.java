package com.example.apiserver.controller;

import com.example.apiserver.dto.MemberListDTO;
import com.example.apiserver.dto.MemberRegisterDTO;
import com.example.apiserver.dto.PagedMemberListDTO;
import com.example.apiserver.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@Controller
@RequestMapping("/api/member/")
@Log4j2
@AllArgsConstructor
public class MemberController {

    MemberService memberService;

    @GetMapping("/{tel}")
    @ResponseBody
    public ResponseEntity<MemberListDTO> getMember(@PathVariable String tel){
        try{
            MemberListDTO memberListDTO = memberService.getMember(tel);
            return ResponseEntity.ok().body(memberListDTO);
        } catch(Exception e){
            log.error("Error in get member {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteMember(@RequestBody MemberListDTO memberListDTO){
        try{
            memberService.deleteMember(memberListDTO.getTel());
            return ResponseEntity.ok().body("deleted :" + memberListDTO.getTel());
        } catch(Exception e){
            log.error("Error in get member {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list/{page}")
    @ResponseBody
    public ResponseEntity<PagedMemberListDTO> listMembers(@PathVariable(required = false) String page){
        if(page==null) page="0";
        //if(tel==null) tel="";
        //log.info("#####listMembers pathVariable : {}, {}", page, tel);

        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        try{
            PagedMemberListDTO pagedMemberListDTO = memberService.getPagedMemberList(pageable);
            log.info("### listMembers : pagedMemberListDTO : {} ", pagedMemberListDTO);
            return ResponseEntity.ok().body(pagedMemberListDTO);
        } catch(Exception e){
            log.error("Error in get list of member", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
