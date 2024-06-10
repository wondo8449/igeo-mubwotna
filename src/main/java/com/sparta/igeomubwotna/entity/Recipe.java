package com.sparta.igeomubwotna.entity;

import com.sparta.igeomubwotna.dto.RecipeRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(name = "recipe")
@NoArgsConstructor
public class Recipe extends Timestamped{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column
	private Long recipeLikes;

	public void addLike() {
		this.recipeLikes = recipeLikes + 1L;
	}

	public void minusLike() {
		this.recipeLikes = recipeLikes - 1L;
	}

	public Recipe(RecipeRequestDto requestDto, User user) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
		this.recipeLikes = 0L;
		this.user = user;
	}

	public void update(RecipeRequestDto requestDto) {
		this.content = requestDto.getContent();
	}
}
