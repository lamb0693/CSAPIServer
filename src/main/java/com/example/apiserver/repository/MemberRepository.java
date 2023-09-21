package com.example.apiserver.repository;

import com.example.apiserver.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    public Optional<MemberEntity> findByTel(String tel);
}
