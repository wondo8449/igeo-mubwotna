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

	public RecipeResponseDto getRecipe(Long recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
			new IllegalArgumentException("선택한 recipe는 존재하지 않습니다.")
		);
		return new RecipeResponseDto(recipe);
	}

	@Transactional
	public RecipeResponseDto updateRecipe(Long recipeId, RecipeRequestDto requestDto, User user) {
		Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
			new IllegalArgumentException("선택한 recipe는 존재하지 않습니다.")
		);

		recipe.update(requestDto);
		return new RecipeResponseDto(recipe);

	}


}
