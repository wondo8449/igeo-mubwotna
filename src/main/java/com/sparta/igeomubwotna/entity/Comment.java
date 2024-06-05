package com.sparta.igeomubwotna.entity;

<<<<<<< HEAD
import com.sparta.igeomubwotna.dto.CommentRequestDto;
=======
>>>>>>> 0d34ead6d506b8b6a87288ed912c44913111456d
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

    //내용
    @Column(nullable = false)
    private String content;

    //좋아요 수
    @Column(nullable = false)
    private Long likeCount;

<<<<<<< HEAD
    /* Mapping */
    //작성자 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //레시피 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    /* Constructor */
    public Comment(CommentRequestDto requestDto, Recipe recipe, User user) {
        this.content = requestDto.getContent();
        this.likeCount = requestDto.getLikeCount();
        this.recipe = recipe;
        this.user = user;
=======
    /* Constructor */
    public Comment(CommentResponseDto requestDto, Recipe recipe) {
        this.content = requestDto.getContent();
        this.userId = requestDto.getUserId();
        this.likeCount = requestDto.getLikeCount();
>>>>>>> 0d34ead6d506b8b6a87288ed912c44913111456d
    }


}
