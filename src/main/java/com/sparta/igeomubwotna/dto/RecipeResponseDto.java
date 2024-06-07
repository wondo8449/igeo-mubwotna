package com.sparta.igeomubwotna.dto;

import java.time.LocalDateTime;

import com.sparta.igeomubwotna.entity.Recipe;

import lombok.Getter;

@Getter
public class RecipeResponseDto {
	String title;
	String content;
	String userId;
	LocalDateTime createdAt;
	LocalDateTime modifiedAt;

	public RecipeResponseDto(Recipe recipe) {
		this.title = recipe.getTitle();
		this.content = recipe.getContent();
		this.userId = recipe.getUser().getUserId();
		this.createdAt = recipe.getCreatedAt();
		this.modifiedAt = recipe.getModifiedAt();
	}
}
