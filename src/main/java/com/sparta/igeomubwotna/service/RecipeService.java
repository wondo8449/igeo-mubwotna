package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Recipe findRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() ->
                new IllegalIdentifierException("존재하지 않는 레시피입니다."));
    }
}
