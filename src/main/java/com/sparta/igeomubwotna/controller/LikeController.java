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
}
