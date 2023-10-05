package com.example.apiserver.dto;

import com.example.apiserver.constant.Content;
import com.example.apiserver.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardListDTO {
    private Long board_id;

    private String name;

    private Content content;

    private String message;

    private LocalDateTime update_date = LocalDateTime.now();

    @Override
    public String toString() {
        return "BoardListDTO{" +
                "board_id=" + board_id +
                ", name='" + name + '\'' +
                ", content=" + content +
                ", message='" + message + '\'' +
                ", update_date=" + update_date +
                '}';
    }
}
