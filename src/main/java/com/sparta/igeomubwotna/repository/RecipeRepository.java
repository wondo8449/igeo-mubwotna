package com.sparta.igeomubwotna.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.igeomubwotna.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
