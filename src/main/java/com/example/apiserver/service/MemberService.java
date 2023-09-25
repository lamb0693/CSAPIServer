package com.example.apiserver.service;

import com.example.apiserver.constant.Role;
import com.example.apiserver.dto.MemberListDTO;
import com.example.apiserver.dto.MemberRegisterDTO;
import com.example.apiserver.dto.PagedMemberListDTO;
import com.example.apiserver.entity.MemberEntity;
import com.example.apiserver.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
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
}
