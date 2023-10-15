package com.example.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    String tel;
    String password;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "tel='" + tel + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
