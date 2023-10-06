package com.example.apiserver.service;

import com.example.apiserver.constant.Content;
import com.example.apiserver.dto.BoardCreateDTO;
import com.example.apiserver.dto.BoardListDTO;
import com.example.apiserver.entity.BoardEntity;
import com.example.apiserver.entity.MemberEntity;
import com.example.apiserver.repository.BoardRepository;
import com.example.apiserver.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Value( "${org.zerock.upload.path}" )
    private String uploadPath;

    public BoardEntity create(BoardCreateDTO boardCreateDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity = memberRepository.findByTel(boardCreateDTO.getTel()).orElseThrow(()->new RuntimeException("tel not found exception"));
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setContent(Content.valueOf( boardCreateDTO.getContent() ));
        boardEntity.setMessage(boardCreateDTO.getMessage());
        boardEntity.setUploader(memberEntity);
        log.info("##### create@BoardService boardEntity {}", boardEntity);

        MultipartFile file = boardCreateDTO.getFile();
        if(file != null){
            String origFilename = file.getOriginalFilename();
            boardEntity.setOrigFilename( origFilename );
            String uuid = UUID.randomUUID().toString();
            Path savedPath = Paths.get(uploadPath + "\\"+ uuid + "_" +  origFilename );
            boardEntity.setFilePath(savedPath.toString());
            try{
                file.transferTo(savedPath);
                log.info("file {} saved to {}", origFilename, savedPath);
            } catch(Exception e){
                log.error("fail to save {} to {}", origFilename, savedPath);
            }
        }

        return boardRepository.save(boardEntity);

    }

    public BoardListDTO list(int noOfDisplay, String tel) {
        BoardListDTO boardListDTO = new BoardListDTO();

        Pageable pageable = PageRequest.of(0, noOfDisplay);
        List<BoardEntity> boardEntityList = boardRepository.list(tel, pageable);
        /*
        Under construction

        */
        return boardListDTO;
    }
}
