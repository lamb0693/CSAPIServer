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

    private String content;

    private boolean bReplied;

    private String message;

    private String strUpdatedAt;

    @Override
    public String toString() {
        return "BoardListDTO{" +
                "board_id=" + board_id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", bReplied=" + bReplied +
                ", message='" + message + '\'' +
                ", strUpdatedAt='" + strUpdatedAt + '\'' +
                '}';
    }
}
