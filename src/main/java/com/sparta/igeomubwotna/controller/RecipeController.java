package com.sparta.igeomubwotna.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.igeomubwotna.dto.RecipeRequestDto;
import com.sparta.igeomubwotna.dto.RecipeResponseDto;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.RecipeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

	private final RecipeService recipeService;

	@PostMapping("/")
	public ResponseEntity<RecipeResponseDto> createRecipe(@Valid @RequestBody RecipeRequestDto requestDto,
		BindingResult bindingResult,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (fieldErrors.size() > 0) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.saveRecipe(requestDto, userDetails.getUser()));

	}

	@GetMapping("/{recipeId}")
	public ResponseEntity<RecipeResponseDto> getRecipe(@PathVariable Long recipeId) {
		return ResponseEntity.status(HttpStatus.OK).body(recipeService.getRecipe(recipeId));
	}

	@PatchMapping("/{recipeId}")
	public ResponseEntity<RecipeResponseDto> editRecipe(@PathVariable Long recipeId,
		@RequestBody RecipeRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.status(HttpStatus.OK).body(recipeService.editRecipe(recipeId, requestDto, userDetails.getUser()));
	}

	@DeleteMapping("/{recipeId}")
	public ResponseEntity deleteRecipe(@PathVariable Long recipeId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.status(HttpStatus.OK).body(recipeService.deleteRecipe(recipeId, userDetails.getUser()));
	}
}
