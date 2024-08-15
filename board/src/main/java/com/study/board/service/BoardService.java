package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 글 작성
    public void write(Board board, @RequestParam(name = "file") MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename(); // file null 이 뜬 이유는 파일이 안담겼다는

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        // 이미지 mysql 내용에 저장
        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);


        boardRepository.save(board);

    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }


    // 특정 게시글 불러오기
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    // 특적 게시글 삭제
    public void boardDelete(@RequestParam Integer id) {

        boardRepository.deleteById(id);
    }
}
