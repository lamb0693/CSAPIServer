package com.example.apiserver.service;

import com.example.apiserver.constant.Role;
import com.example.apiserver.dto.MemberListDTO;
import com.example.apiserver.dto.MemberRegisterDTO;
import com.example.apiserver.entity.MemberEntity;
import com.example.apiserver.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;

    // 새로운 멤버 가입
    public MemberRegisterDTO register(@RequestBody MemberRegisterDTO memberRegisterDTO)
            throws IllegalArgumentException, OptimisticLockingFailureException{

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setTel(memberRegisterDTO.getTel());
        memberEntity.setName(memberRegisterDTO.getName());
        memberEntity.setRole(Role.valueOf("USER"));
        memberEntity.setPassword(passwordEncoder.encode(memberRegisterDTO.getPassword()));
        MemberEntity saved = memberRepository.save(memberEntity);

        MemberRegisterDTO saveResultDTO = new MemberRegisterDTO();
        saveResultDTO.setName(saved.getName());
        saveResultDTO.setTel(saved.getTel());

        return saveResultDTO;
    }

    public MemberListDTO getMember(String tel) throws Exception{
        MemberListDTO memberListDTO = new MemberListDTO();

        Optional<MemberEntity> optionalMember= memberRepository.findByTel(tel);
        MemberEntity memberEntity = optionalMember.orElseThrow(() -> { return new RuntimeException("member not found by tel Excepton"); } );

        memberListDTO.setName(memberEntity.getName());
        memberListDTO.setTel(memberEntity.getTel());
        memberListDTO.setRole(memberEntity.getRole().toString());
        memberListDTO.setJoin_date(memberEntity.getJoin_date());

        return memberListDTO;
    }

    public void deleteMember(String tel) throws Exception{
        Optional<MemberEntity> optionalMember= memberRepository.findByTel(tel);
        MemberEntity memberEntity = optionalMember.orElseThrow(() -> { return new RuntimeException("member not found by tel Excepton"); } );

        memberRepository.delete(memberEntity);
    }
}
