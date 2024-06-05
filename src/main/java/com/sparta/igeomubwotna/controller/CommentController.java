package com.sparta.igeomubwotna.controller;

<<<<<<< HEAD
import com.sparta.igeomubwotna.dto.CommentRequestDto;
=======
>>>>>>> 0d34ead6d506b8b6a87288ed912c44913111456d
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
<<<<<<< HEAD
    public CommentResponseDto createComment (@RequestBody CommentRequestDto requestDto,
                                             @PathVariable Long recipeId,
                                             @PathVariable Long userId) {
        return commentService.createComment(requestDto, recipeId, userId);
=======
    public CommentResponseDto createComment (@RequestBody CommentResponseDto requestDto, @PathVariable Long recipeId) {
        return commentService.createComment(requestDto, recipeId);
>>>>>>> 0d34ead6d506b8b6a87288ed912c44913111456d
    }

}
