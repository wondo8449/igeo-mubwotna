package com.sparta.igeomubwotna.dto;

import java.time.LocalDateTime;

import com.sparta.igeomubwotna.entity.Recipe;

public class RecipeResponseDto {
	String title;
	String content;
	LocalDateTime createdAt;
	LocalDateTime modifiedAt;

	public RecipeResponseDto(Recipe recipe) {
		this.title = recipe.getTitle();
		this.content = recipe.getContent();
		this.createdAt = recipe.getCreatedAt();
		this.modifiedAt = recipe.getModifiedAt();
	}
}
