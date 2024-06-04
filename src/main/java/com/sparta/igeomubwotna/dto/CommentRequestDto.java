package com.sparta.igeomubwotna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private Long userId;
    private String content;
    private Long likeCount;

}
