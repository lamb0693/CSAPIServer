package com.example.apiserver.service;

import com.example.apiserver.constant.Content;
import com.example.apiserver.dto.BoardCreateDTO;
import com.example.apiserver.dto.BoardListDTO;
import com.example.apiserver.dto.PagedBoardListDTO;
import com.example.apiserver.entity.BoardEntity;
import com.example.apiserver.entity.MemberEntity;
import com.example.apiserver.repository.BoardRepository;
import com.example.apiserver.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    @Transactional
    public BoardEntity create(BoardCreateDTO boardCreateDTO){
        MemberEntity customerEntity = new MemberEntity();
        customerEntity = memberRepository.findByTel(boardCreateDTO.getCustomerTel()).orElseThrow(()->new RuntimeException("customer tel not found exception"));
        MemberEntity memberEntity = new MemberEntity();
        memberEntity = memberRepository.findByTel(boardCreateDTO.getTel()).orElseThrow(()->new RuntimeException("tel not found exception"));
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setContent(Content.valueOf( boardCreateDTO.getContent() ));
        boardEntity.setMessage(boardCreateDTO.getMessage());
        boardEntity.setUploader(memberEntity);
        boardEntity.setCustomer(customerEntity);
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

    public List<BoardListDTO> list(int noOfDisplay, String tel) {
        List<BoardListDTO> boardListDTOs = new ArrayList<>();
        BoardListDTO boardListDTO = null;

        Pageable pageable = PageRequest.of(0, noOfDisplay);
        List<BoardEntity> boardEntityList = boardRepository.list(tel, pageable);
        log.info("#### list@BoardService boardEntityList : {}", boardEntityList);
        for(BoardEntity boardEntity : boardEntityList){
            boardListDTO = new BoardListDTO();
            boardListDTO.setBoard_id(boardEntity.getBoard_id());
            boardListDTO.setName(boardEntity.getUploader().getName());
            boardListDTO.setTel(boardEntity.getUploader().getTel());
            boardListDTO.setBReplied(boardEntity.isBReplied());
            boardListDTO.setContent(boardEntity.getContent().toString());
            boardListDTO.setMessage(boardEntity.getMessage());
            boardListDTO.setStrUpdatedAt(boardEntity.getUpdate_date().toString());
            boardListDTOs.add(boardListDTO);
        }
        log.info("#### list@BoardService boardListDTOS : {}", boardListDTOs);
        return boardListDTOs;
    }

    public PagedBoardListDTO listUnreplied(int page) {
        PagedBoardListDTO pagedBoardListDTO = new PagedBoardListDTO();
        List<BoardListDTO> boardListDTOs = new ArrayList<>();
        BoardListDTO boardListDTO = null;

        Pageable pageable = PageRequest.of(page, 10);
        Page<BoardEntity> boardEntityList = boardRepository.findAllByBReplied(false, pageable);
        log.info("#### list@BoardService boardEntityList : {}", boardEntityList);
        for(BoardEntity boardEntity : boardEntityList){
            if(boardEntity.getUploader().getRole().toString().equals("CSR")) continue;
            boardListDTO = new BoardListDTO();
            boardListDTO.setBoard_id(boardEntity.getBoard_id());
            boardListDTO.setName(boardEntity.getUploader().getName());
            boardListDTO.setTel(boardEntity.getUploader().getTel());
            boardListDTO.setBReplied(boardEntity.isBReplied());
            boardListDTO.setContent(boardEntity.getContent().toString());
            boardListDTO.setMessage(boardEntity.getMessage());
            boardListDTO.setStrUpdatedAt(boardEntity.getUpdate_date().toString());
            boardListDTOs.add(boardListDTO);
        }
        pagedBoardListDTO.setMemberListDTOList(boardListDTOs);
        pagedBoardListDTO.setCurrentPage(boardEntityList.getNumber());
        pagedBoardListDTO.setPageSize(boardEntityList.getTotalPages());
        pagedBoardListDTO.setTotalElements(boardEntityList.getTotalElements());
        log.info("#### listUnreplied@BoardService boardListDTOS : {}", boardListDTOs);
        return pagedBoardListDTO;
    }

    public String getFilePath(Long id) throws Exception {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->new RuntimeException("id not found"));
        return boardEntity.getFilePath();
    }
}
