package com.example.apiserver.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BoardCreateDTO {
    String tel;
    String content;
    String message;
    MultipartFile file;

    @Override
    public String toString() {
        return "BoardCreateDTO{" +
                "tel='" + tel + '\'' +
                ", content='" + content + '\'' +
                ", message='" + message + '\'' +
                ", file=" + file +
                '}';
    }
}
