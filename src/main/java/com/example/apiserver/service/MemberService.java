package com.example.apiserver.service;

import com.example.apiserver.constant.Role;
import com.example.apiserver.dto.*;
import com.example.apiserver.entity.MemberEntity;
import com.example.apiserver.repository.MemberRepository;
import com.example.apiserver.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class MemberService {
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;

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


    private MemberListDTO entityToDTO(MemberEntity memberEntity){
        MemberListDTO memberListDTO = new MemberListDTO();
        memberListDTO.setName(memberListDTO.getName());
        memberListDTO.setTel(memberEntity.getTel());
        memberListDTO.setRole(memberEntity.getRole().name());
        memberListDTO.setJoin_date(memberEntity.getJoin_date());
        //log.info("entityToDTO memberListDTO {}", memberListDTO);
        return memberListDTO;
    }

    public PagedMemberListDTO getPagedMemberList(Pageable pageable) {

        List<MemberListDTO> memberDTOList = new ArrayList<>();

        Page<MemberEntity> memberEntityList = memberRepository.findAll(pageable);

        for(MemberEntity memberEntity : memberEntityList){
            log.info(memberEntity);
            memberDTOList.add( entityToDTO(memberEntity) );
            //log.info("memberDTOLIST {}", memberDTOList);
        }
        //log.info("####getPagedMemberList : memberDTOList {}", memberDTOList);

        PagedMemberListDTO pagedMemberListDTO = new PagedMemberListDTO();
        pagedMemberListDTO.setMemberListDTOList(memberDTOList);
        pagedMemberListDTO.setCurrentPage(memberEntityList.getNumber());
        pagedMemberListDTO.setPageSize(memberEntityList.getTotalPages());
        pagedMemberListDTO.setTotalElements(memberEntityList.getTotalElements());

        return pagedMemberListDTO;

    }

    // password 확인 과정을 다시 만들어야
    public String getToken(LoginDTO loginDTO){
        String token = null;

        try{
            MemberEntity memberEntity = memberRepository.findByTel(loginDTO.getTel()).orElseThrow(()->{throw new RuntimeException("member not found");});
            if(loginDTO.getPassword().equals("00000000")){
                Map<String, Object> claim = Map.of("tel", memberEntity.getTel(), "authorities", memberEntity.getRole().toString());
                String accessToken = jwtUtil.generateToken(claim, 2*60*24); //2일
                return accessToken;
            } else {
                return "password not correct";
            }
        } catch(Exception e){
            return e.getMessage();
        }
    }

    // ************* password 검증 추가 해야 함 ***************/
    public UserInfoDTO getUserInfo(LoginDTO loginDTO) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();

        MemberEntity memberEntity = memberRepository.findByTel(loginDTO.getTel()).orElseThrow(()->{throw new RuntimeException("member not found");});
        if(loginDTO.getPassword().equals("00000000")){
            Map<String, Object> claim = Map.of("tel", memberEntity.getTel(), "authorities", memberEntity.getRole().toString());
            userInfoDTO.setAccessToken(jwtUtil.generateToken(claim, 2*60*24)); //2일
            userInfoDTO.setTel(memberEntity.getTel());
            userInfoDTO.setRole(memberEntity.getRole().toString());
            return userInfoDTO;
        } else {
            throw new RuntimeException("password not correct");
        }
    }
}
