package com.sparta.igeomubwotna.entity;

import com.sparta.igeomubwotna.dto.CommentResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped{

    /* Column */
    //ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //레시피 ID
    @Column(nullable = false)
    private Long recipeId;

    //작성자 ID
    @Column(nullable = false)
    private Long userId;

    //내용
    @Column(nullable = false)
    private String content;

    //좋아요 수
    @Column(nullable = false)
    private Long likeCount;

    /* Constructor */
    public Comment(CommentResponseDto requestDto, Recipe recipe) {
        this.content = requestDto.getContent();
        this.userId = requestDto.getUserId();
        this.likeCount = requestDto.getLikeCount();
    }


}
