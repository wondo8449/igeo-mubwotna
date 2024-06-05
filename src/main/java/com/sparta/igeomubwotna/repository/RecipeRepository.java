package com.sparta.igeomubwotna.repository;

import com.sparta.igeomubwotna.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
