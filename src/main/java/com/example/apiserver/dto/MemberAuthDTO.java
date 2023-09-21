package com.example.apiserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class MemberAuthDTO extends User {
    private String tel;
    private String password;
    public MemberAuthDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.tel = username;
        this.password = password;
    }
}
