package com.sparta.igeomubwotna.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
public class Response {
    private int statusCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL) // null 값일 경우 직렬화에서 제외
    private List<String> errors;  // 필드 오류 메시지를 담을 리스트

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = null; // 성공 시 errors 필드는 null로 설정
    }

    public Response(int statusCode, String message, List<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }
}
