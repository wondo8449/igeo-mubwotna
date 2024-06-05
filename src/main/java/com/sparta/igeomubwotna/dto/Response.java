package com.sparta.igeomubwotna.dto;

import lombok.Getter;

import java.util.List;
@Getter
public class Response {
    private int statusCode;
    private String message;
    private List<String> errors;  // 필드 오류 메시지를 담을 리스트

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Response(int statusCode, String message, List<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }
}
