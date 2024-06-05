package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.dto.CommentRequestDto;
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
    public CommentResponseDto createComment (@RequestBody CommentRequestDto requestDto,
                                             @PathVariable Long recipeId,
                                             @PathVariable Long userId) {
        return commentService.createComment(requestDto, recipeId, userId);
    }

}
