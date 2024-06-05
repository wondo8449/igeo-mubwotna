package com.sparta.igeomubwotna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.igeomubwotna.dto.RecipeRequestDto;
import com.sparta.igeomubwotna.dto.RecipeResponseDto;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.RecipeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecipeService {

	private final RecipeRepository recipeRepository;

	@Transactional
	public RecipeResponseDto saveRecipe(RecipeRequestDto requestDto, User user) {
		Recipe recipe = recipeRepository.save(new Recipe(requestDto, user));
		return new RecipeResponseDto(recipe);
	}
}
