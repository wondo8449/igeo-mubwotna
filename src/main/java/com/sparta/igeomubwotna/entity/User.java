package com.sparta.igeomubwotna.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@Size(min = 10, max = 20, message = "사용자 ID는 최소 10글자 이상, 20글자 이하이어야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "사용자 ID는 알파벳 대소문자, 숫자로만 구성되어야 합니다.")
	private String userId;

	@Size(min = 10, message = "password는 최소 10글자 이상이어야 합니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_!#$%&'*+/=?`{|}~^.-])[a-zA-Z\\d]$", message = "password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로만 구성되어야 합니다.")
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String description;

	// @Column(nullable = false)
	// @Enumerated(value = EnumType.STRING)
	// private UserStatusEnum status;

	@LastModifiedDate
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime statusModifiedAt;


	// @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	// private List<Recipe> recipes = new ArrayList<>();
	//
	// @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	// private List<Comment> comments = new ArrayList<>();


	// 생성자
}