package com.example.apiserver.entity;

import com.example.apiserver.constant.Content;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Table(name="boards")
@Data
@Entity
public class BoardEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long board_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity uploader;

    @Column(nullable = false)
    private boolean bReplied = false;

    @Column(length=10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Content content;

    @Column(length = 100)
    private String message;

    @Column(length = 100)
    String filePath;

    @Column(length = 100)
    String origFilename;

    @Column(updatable = false)
    @CreationTimestamp()
    private LocalDateTime join_date = LocalDateTime.now();

    @Column
    @CreationTimestamp
    private LocalDateTime update_date = LocalDateTime.now();

}
