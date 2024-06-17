package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.dto.RecipeRequestDto;
import com.sparta.igeomubwotna.dto.RecipeResponseDto;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;

    @Test
    void saveRecipeTest() {
        // given
        Recipe recipe = new Recipe(new User(), "레시피 제목", "레시피 내용");
        RecipeResponseDto responseDto = new RecipeResponseDto(recipe);

        ResponseEntity responseEntity = new ResponseEntity(responseDto, HttpStatus.CREATED);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        // when
        RecipeService recipeService = new RecipeService(recipeRepository);
        ResponseEntity result = recipeService.saveRecipe(new RecipeRequestDto("레시피 제목", "레시피 내용"), any(User.class));

        // then
        assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
        assertEquals(responseEntity.getBody().getClass(), result.getBody().getClass());
    }

    @Test
    void getRecipeTest() {
        // given
        Recipe recipe = new Recipe(new User(), "레시피 제목", "레시피 내용");
        RecipeResponseDto responseDto = new RecipeResponseDto(recipe);

        ResponseEntity responseEntity = new ResponseEntity(responseDto, HttpStatus.OK);

        given(recipeRepository.findById(any(Long.class))).willReturn(Optional.of(recipe));

        // when
        RecipeService recipeService = new RecipeService(recipeRepository);
        ResponseEntity result = recipeService.getRecipe(any(Long.class));

        // then
        assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
    }

    @Test
    void editRecipeTest() {
        // given
        RecipeRequestDto requestDto = new RecipeRequestDto("수정된 레시피 제목", "수정된 레시피 내용");
        User user = new User(1L ,"as", "as", "as", "as", "as");
        Recipe recipe = new Recipe(1L, requestDto, user);
        RecipeResponseDto responseDto = new RecipeResponseDto(recipe);

        ResponseEntity responseEntity = new ResponseEntity(responseDto, HttpStatus.OK);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
        when(recipeRepository.findById(any(Long.class))).thenReturn(Optional.of(recipe));
        // when
        RecipeService recipeService = new RecipeService(recipeRepository);

        ResponseEntity result = recipeService.editRecipe(1L, requestDto, user);

        // then
        assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
    }
}
