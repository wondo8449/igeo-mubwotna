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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	private Long recipeLikes = 0L;

	public Recipe(RecipeRequestDto requestDto, User user) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
		this.user = user;
	}

	public void update(RecipeRequestDto requestDto) {

		if (requestDto.getTitle() != null) {
			this.title = requestDto.getTitle();
		}
		if (requestDto.getContent() != null) {
			this.content = requestDto.getContent();
		}

	}
}
