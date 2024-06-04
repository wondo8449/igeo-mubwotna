package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeService recipeService;

    /* Create */
    public CommentResponseDto createComment(CommentResponseDto requestDto, Long recipeId) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        Comment comment = new Comment(requestDto, recipe);

        commentRepository.save(comment);
        return new CommentResponseDto("comment가 등록되었습니다.");
    }
}
