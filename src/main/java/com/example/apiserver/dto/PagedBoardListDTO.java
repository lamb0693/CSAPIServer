package com.example.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedBoardListDTO {
    List<BoardListDTO> memberListDTOList;

    private int currentPage;
    private int pageSize;
    private long totalElements;

    @Override
    public String toString() {
        return "PagedBoardListDTO{" +
                "memberListDTOList=" + memberListDTOList +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                '}';
    }
}
