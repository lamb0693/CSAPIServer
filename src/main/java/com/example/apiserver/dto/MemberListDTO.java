package com.example.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberListDTO {
    private String tel;
    private String name;

    private String role;
    private LocalDateTime join_date;

    @Override
    public String toString() {
        return "MemberListDTO{" +
                "tel='" + tel + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", join_date='" + join_date + '\'' +
                '}';
    }
}
