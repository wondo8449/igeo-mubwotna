package com.sparta.igeomubwotna.repository;

import com.sparta.igeomubwotna.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void findByRecipeId(Long recipeId);
}
