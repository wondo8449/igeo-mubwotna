package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.entity.*;
import com.sparta.igeomubwotna.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final RecipeLikesRepository recipeLikesRepository;
    private final CommentLikesRepository commentLikesRepository;
    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public ResponseEntity addRecipeLike(Long recipeId, User user) {

        User foundUser = userRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        Recipe foundRecipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다."));

        if (foundUser.getUserId() == foundRecipe.getUser().getUserId()) {
            new IllegalArgumentException("자신이 작성한 레시피에는 좋아요를 남길 수 없습니다.");
        }

        var RecipeLikes = new RecipeLikes(foundUser, foundRecipe);

        if(recipeLikesRepository.findByUserAndRecipe(foundUser, foundRecipe).isPresent()) {
            new IllegalArgumentException("이미 좋아요를 누른 레시피입니다.");
        }

        recipeLikesRepository.save(RecipeLikes);

        return ResponseEntity.status(200).body("좋아요 성공!");
    }

    @Transactional
    public ResponseEntity removeRecipeLike(Long recipeLikeId, User user) {

        RecipeLikes foundlike = recipeLikesRepository.findById(recipeLikeId).orElseThrow(
                () -> new IllegalArgumentException("해당 좋아요가 존재하지 않습니다."));

        recipeLikesRepository.delete(foundlike);

        return ResponseEntity.status(200).body("좋아요 취소 성공!");
    }

    public ResponseEntity addCommentLike(Long commentId, User user) {

        User foundUser = userRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        Comment foundComment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        if (foundUser.getUserId() == foundComment.getUser().getUserId()) {
            new IllegalArgumentException("자신이 작성한 댓글에는 좋아요를 남길 수 없습니다.");
        }

        if(commentLikesRepository.findByUserAndComment(foundUser, foundComment).isPresent()) {
            new IllegalArgumentException("이미 좋아요를 누른 댓글입니다.");
        }

        var CommentLikes = new CommentLikes(foundUser, foundComment);

        commentLikesRepository.save(CommentLikes);

        return ResponseEntity.status(200).body("좋아요 성공!");
    }

    @Transactional
    public ResponseEntity removeCommentLike(Long commentId, User user) {

        CommentLikes foundLike = commentLikesRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 좋아요가 존재하지 않습니다."));

        commentLikesRepository.delete(foundLike);

        return ResponseEntity.status(200).body("좋아요 취소 성공!");
    }

//    public Long getLike(Recipe recipe) {
//        return recipeLikesRepository.countByRecipe(recipe);
//    }
//
//    public
//    Long getLike(Comment comment) {
//        return commentLikesRepository.countByComment(comment);
//    }
}
