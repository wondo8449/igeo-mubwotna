package com.sparta.igeomubwotna.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String message;

    public CommentResponseDto(String message) {
        this.message = message;
    }
}
