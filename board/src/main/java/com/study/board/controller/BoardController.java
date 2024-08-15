package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") // localhost:8090/board/write
    public String boardWriteForm() {

        return "boardwrite";

    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, @RequestParam(name = "file") MultipartFile file) throws Exception {

        boardService.write(board, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @PostMapping("/filetest")
    public String addImage2(@RequestParam("Photo") MultipartFile uploadFile,
                            HttpServletRequest request) {
        String fileName = uploadFile.getOriginalFilename();
        String filePath = request.getSession().getServletContext().getRealPath("/src/main/webapp");
        try {
            uploadFile.transferTo(new File(filePath + fileName));
            System.out.println("이미지 파일 저장 완료");
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return " 파일 저장 완료";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword) {
        Page<Board> list = null;

        if (searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, @RequestParam(name = "id") Integer id) {
        model.addAttribute("board", boardService.boardView(id));

        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam(name = "id") Integer id) {

        boardService.boardDelete(id);

        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(Model model, @PathVariable("id") Integer id) {

        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }


    @PostMapping("/board/update/{id}")
    public String boardUpdate(Board board, @PathVariable("id") Integer id, @RequestParam(name = "file") MultipartFile file) throws Exception {


        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);


        return "redirect:/board/list";
    }
}

