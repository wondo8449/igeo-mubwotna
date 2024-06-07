package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.dto.CommentRequestDto;
import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /* Update : 댓글 수정*/
    @Transactional
    public CommentResponseDto UpdateComment(Long recipeId, Long commentId, CommentRequestDto requestDto) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        Comment comment = findById(commentId);

        if (!Objects.equals(comment.getUser().getUserId(), requestDto.getUserId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        comment.update(requestDto.getContent());
        return CommentResponseDto.toDto(comment);

    }

    /* Delete : 댓글 삭제 */
    public void deleteComment(Long recipeId, Long commentId, CommentRequestDto requestDto) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        Comment comment = findById(commentId);

        if (!Objects.equals(comment.getUser().getUserId(), requestDto.getUserId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }


    /* ID로 comment 찾기 */
    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
    }

}
