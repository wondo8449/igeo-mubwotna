package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.RecipeLikes;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.CommentLikesRepository;
import com.sparta.igeomubwotna.repository.RecipeLikesRepository;
import com.sparta.igeomubwotna.repository.RecipeRepository;
import com.sparta.igeomubwotna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final RecipeLikesRepository recipeLikesRepository;
    private final CommentLikesRepository commentLikesRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public ResponseEntity addRecipeLike(Long recipeId, User user) {

        User foundUser = userRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다."));

        Recipe foundRecipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다."));

        var RecipeLikes = new RecipeLikes(foundUser, foundRecipe);

        recipeLikesRepository.save(RecipeLikes);

        return ResponseEntity.status(200).body("좋아요 성공!");
    }
}
