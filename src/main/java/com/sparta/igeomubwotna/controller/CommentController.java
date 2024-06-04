package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipe/{recipeId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /* Create */
    @PostMapping
    public CommentResponseDto createComment (@RequestBody CommentResponseDto requestDto, @PathVariable Long recipeId) {
        return commentService.createComment(requestDto, recipeId);
    }

}
