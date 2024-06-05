package com.sparta.igeomubwotna.service;

<<<<<<< HEAD
import com.sparta.igeomubwotna.dto.CommentRequestDto;
import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
=======
import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
>>>>>>> 0d34ead6d506b8b6a87288ed912c44913111456d
import com.sparta.igeomubwotna.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeService recipeService;
<<<<<<< HEAD
    private final UserService userService;

    /* Create */
    public CommentResponseDto createComment(CommentRequestDto requestDto, Long recipeId, Long userId) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        User user = userService.findUserById(userId);
        Comment comment = new Comment(requestDto, recipe, user);
=======

    /* Create */
    public CommentResponseDto createComment(CommentResponseDto requestDto, Long recipeId) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        Comment comment = new Comment(requestDto, recipe);
>>>>>>> 0d34ead6d506b8b6a87288ed912c44913111456d

        commentRepository.save(comment);
        return new CommentResponseDto("comment가 등록되었습니다.");
    }
}
