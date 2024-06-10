package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.entity.*;
import com.sparta.igeomubwotna.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final RecipeLikesRepository recipeLikesRepository;
    private final CommentLikesRepository commentLikesRepository;
    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity addRecipeLike(Long recipeId, User user) {

        User foundUser = userRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        Recipe foundRecipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다."));

        if (foundUser.getUserId().equals(foundRecipe.getUser().getUserId())) {
            throw new IllegalArgumentException("자신이 작성한 레시피에는 좋아요를 남길 수 없습니다.");
        }

        var RecipeLikes = new RecipeLikes(foundUser, foundRecipe);

        Optional<RecipeLikes> list = recipeLikesRepository.findByUserAndRecipe(foundUser, foundRecipe);

        System.out.println(list);

        if(!(recipeLikesRepository.findByUserAndRecipe(foundUser, foundRecipe).isEmpty())) {
            throw new IllegalArgumentException("이미 좋아요를 누른 레시피입니다.");
        }

        recipeLikesRepository.save(RecipeLikes);

        foundRecipe.addLike();

        return ResponseEntity.status(200).body("좋아요 성공!");
    }

    @Transactional
    public ResponseEntity removeRecipeLike(Long recipeLikeId, User user) {

        RecipeLikes foundlike = recipeLikesRepository.findById(recipeLikeId).orElseThrow(
                () -> new IllegalArgumentException("해당 좋아요가 존재하지 않습니다."));

        Recipe foundRecipe = recipeRepository.findById(foundlike.getRecipe().getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 레시피는 존재하지 않습니다."));

        recipeLikesRepository.delete(foundlike);

        foundRecipe.minusLike();

        return ResponseEntity.status(200).body("좋아요 취소 성공!");
    }

    @Transactional
    public ResponseEntity addCommentLike(Long commentId, User user) {

        User foundUser = userRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        Comment foundComment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        if (foundUser.getUserId().equals(foundComment.getUser().getUserId())) {
            throw new IllegalArgumentException("자신이 작성한 댓글에는 좋아요를 남길 수 없습니다.");
        }

        if(commentLikesRepository.findByUserAndComment(foundUser, foundComment).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 누른 댓글입니다.");
        }

        var CommentLikes = new CommentLikes(foundUser, foundComment);

        commentLikesRepository.save(CommentLikes);

        foundComment.addLike();

        return ResponseEntity.status(200).body("좋아요 성공!");
    }

    @Transactional
    public ResponseEntity removeCommentLike(Long commentId, User user) {

        CommentLikes foundLike = commentLikesRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 좋아요가 존재하지 않습니다."));

        Comment foundComment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        commentLikesRepository.delete(foundLike);

        foundComment.minusLike();

        return ResponseEntity.status(200).body("좋아요 취소 성공!");
    }
}
