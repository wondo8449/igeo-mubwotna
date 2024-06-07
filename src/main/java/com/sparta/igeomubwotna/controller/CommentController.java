package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.dto.CommentRequestDto;
import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.dto.Response;
import com.sparta.igeomubwotna.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe/{recipeId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /* Create */
    @PostMapping("/{userId}")
    public CommentResponseDto createComment (@RequestBody CommentRequestDto requestDto,
                                             @PathVariable Long recipeId,
                                             @PathVariable Long userId) {
        return commentService.createComment(requestDto, recipeId, userId);
    }

    /* Read */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComment (@PathVariable Long recipeId) {
        return ResponseEntity.ok().body(commentService.getComment(recipeId));
    }

    /* Update */
    @PatchMapping("/{commentId}")
    public CommentResponseDto updateComment (@PathVariable Long recipeId,
                                             @PathVariable Long commentId,
                                             @RequestBody CommentRequestDto requestDto) {
        return commentService.UpdateComment(recipeId, commentId, requestDto);
    }

    /* Delete */
    @DeleteMapping("/{commentId}")
    public ResponseEntity delete (@PathVariable Long recipeId,
                            @PathVariable Long commentId,
                            @RequestBody CommentRequestDto requestDto) {
        commentService.deleteComment(recipeId, commentId, requestDto);

        Response response = new Response(HttpStatus.OK.value(), "댓글이 삭제되었습니다");
        return ResponseEntity.ok().body(response);
    }

}
