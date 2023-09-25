package com.example.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedMemberListDTO {
    List<MemberListDTO> memberListDTOList;

    private int currentPage;
    private int pageSize;
    private long totalElements;

    @Override
    public String toString() {
        return "PagedMemberListDTO{" +
                "memberListDTOList=" + memberListDTOList +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                '}';
    }
}
