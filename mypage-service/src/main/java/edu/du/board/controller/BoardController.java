package edu.du.board.controller;

import edu.du.board.model.Board;
import edu.du.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<Page<Board>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(boardService.findAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> findById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findById(id));
    }
} 