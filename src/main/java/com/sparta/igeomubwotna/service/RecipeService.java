package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public Recipe findById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
    }
}