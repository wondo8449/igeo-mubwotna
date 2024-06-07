package com.sparta.igeomubwotna.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	private final LikeService likeService;

	@Transactional
	public RecipeResponseDto saveRecipe(RecipeRequestDto requestDto, User user) {
		Recipe recipe = recipeRepository.save(new Recipe(requestDto, user));
		return new RecipeResponseDto(recipe);
	}

	public RecipeResponseDto getRecipe(Long recipeId) {
		Recipe recipe = findById(recipeId);
		recipe.setRecipeLikes(likeService.getLike(recipe));
		return new RecipeResponseDto(recipe);
	}

	@Transactional
	public RecipeResponseDto editRecipe(Long recipeId, RecipeRequestDto requestDto, User user) {
		Recipe recipe = findById(recipeId);

		// 사용자가 일치하지 않는 경우
		if (!(recipe.getUser().getId().equals(user.getId()))) {
			throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
		}

		recipe.update(requestDto);
		return new RecipeResponseDto(recipe);

	}

	public String deleteRecipe(Long recipeId, User user) {
		Recipe recipe = findById(recipeId);

		// 사용자가 일치하지 않는 경우
		if (!(recipe.getUser().getId().equals(user.getId()))) {
			throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
		}

		recipeRepository.delete(recipe);

		return (recipeId + " 번 삭제 완료");
	}

	public Page<RecipeResponseDto> getAllRecipe(int page, String sortBy) {
		Sort sort = Sort.by(Sort.Direction.DESC, sortBy); // 지금 AscDesc 안 정함, 내가 일단 해야하는 건 최신순 정렬
		Pageable pageable = PageRequest.of(page, 10, sort);

		Page<Recipe> recipeList = recipeRepository.findAll(pageable);

		return recipeList.map(RecipeResponseDto::new);
	}

	public Recipe findById(Long recipeId) {
		return recipeRepository.findById(recipeId).orElseThrow(() ->
			new IllegalArgumentException("해당 레시피가 존재하지 않습니다.")
		);
	}


}
