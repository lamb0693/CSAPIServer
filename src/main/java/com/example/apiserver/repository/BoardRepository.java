package com.example.apiserver.repository;

import com.example.apiserver.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Query(value = "select b from BoardEntity as b where (b.customer.tel = :tel ) order by b.board_id desc")
    List<BoardEntity> list(String tel, Pageable pageable);

    @Query(value = "select b from BoardEntity as b where (b.bReplied = false ) order by b.board_id desc")
    List<BoardEntity> findAllByBReplied(boolean b, Pageable pageable);
}
