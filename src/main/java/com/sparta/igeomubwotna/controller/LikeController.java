package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity addRecipeLike(@PathVariable Long recipeId, User user) {
        return likeService.addRecipeLike(recipeId, user);
    }

    @DeleteMapping("/recipe/{recipeLikeId}")
    public ResponseEntity removeRecipeLike(@PathVariable Long recipeLikeId, User user) {
        return likeService.removeRecipeLike(recipeLikeId, user);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity addCommentLike(@PathVariable Long commentId, User user) {
        return likeService.addCommentLike(commentId, user);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity removeCommentLike(@PathVariable Long commentId, User user) {
        return likeService.removeCommentLike(commentId, user);
    }
}
