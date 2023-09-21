package com.example.apiserver.entity;

import com.example.apiserver.constant.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name="member")
@Data
@Entity
public class MemberEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false , length = 12)
    private String tel;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length=10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(updatable = false)
    @CreationTimestamp()
    private LocalDateTime join_date = LocalDateTime.now();

    @Column
    @CreationTimestamp
    private LocalDateTime update_date = LocalDateTime.now();
}
