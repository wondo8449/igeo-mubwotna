package com.sparta.igeomubwotna.repository;

import com.sparta.igeomubwotna.entity.RecipeLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeLikesRepository extends JpaRepository<RecipeLikes, Long> {
}
