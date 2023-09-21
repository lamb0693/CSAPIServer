package com.example.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterDTO {
    private String tel;
    private String name;
    private String password;

    @Override
    public String toString() {
        return "MemberRegisterDTO{" +
                "tel='" + tel + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
