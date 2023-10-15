package com.example.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {
    String accessToken;
    String tel;
    String role;

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", tel='" + tel + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
