package com.example.apiserver.service;

import com.example.apiserver.dto.MemberAuthDTO;
import com.example.apiserver.entity.MemberEntity;
import com.example.apiserver.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class MemberUserDetailsService implements UserDetailsService{
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> optionalMember = memberRepository.findByTel(username);

        MemberEntity memberEntity = optionalMember.orElseThrow(()->{return new UsernameNotFoundException("가입된 전화 번호가 없어요");});

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + memberEntity.getRole().toString()) );

        MemberAuthDTO memberAuthDTO = new MemberAuthDTO(
                memberEntity.getTel(),
                memberEntity.getPassword(),
                authorityList
        );

        log.info("###### memberAuthoDTO {}", memberAuthDTO);

        return memberAuthDTO;
    }
}
