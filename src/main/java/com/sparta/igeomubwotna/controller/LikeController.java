package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.security.UserDetailsImpl;
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

    @PostMapping("/recipe/{recipeId}")
    public ResponseEntity addRecipeLike(@PathVariable Long recipeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.addRecipeLike(recipeId, userDetails.getUser());
    }

    @DeleteMapping("/recipe/{recipeLikeId}")
    public ResponseEntity removeRecipeLike(@PathVariable Long recipeLikeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.removeRecipeLike(recipeLikeId, userDetails.getUser());
    }

    @PostMapping("/comment/{commentId}")
    public ResponseEntity addCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.addCommentLike(commentId, userDetails.getUser());
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity removeCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.removeCommentLike(commentId, userDetails.getUser());
    }
}
