package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.dto.CommentRequestDto;
import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeService recipeService;
    private final UserService userService;

    /* Create : 댓글 작성 */
    public CommentResponseDto createComment(CommentRequestDto requestDto, Long recipeId, Long userId) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        User user = userService.findUserById(userId);
        Comment comment = new Comment(requestDto, recipe, user);

        commentRepository.save(comment);
        return new CommentResponseDto("comment가 등록되었습니다.");
    }

    /* Read : 댓글 조회 (레시피에 대한 전체 댓글) */
    public List<CommentResponseDto> getComment(Long recipeId) {
        Recipe recipe = recipeService.findRecipeById(recipeId);

        List<Comment> commentList = commentRepository.findByRecipeId(recipeId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(CommentResponseDto.toDto(comment));
        }
        return commentResponseDtoList;
    }


    /* ID로 comment 찾기 */
    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
    }

}
