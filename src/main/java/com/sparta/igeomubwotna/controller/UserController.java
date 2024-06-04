package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.dto.SignupRequestDto;
import com.sparta.igeomubwotna.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/user/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // 유효성 검사 예외 처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        Map<String, Object> response = new HashMap<>();

        // 유효성 검사에 오류가 있으면
        if(fieldErrors.size() > 0) {
            // 모든 필드 오류에 대해 로그 기록
            for(FieldError fieldError: bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드: " + fieldError.getDefaultMessage());
            }
            // 오류 메시지와 상태 코드 반환
            response.put("message", "회원가입에 실패하였습니다.");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", fieldErrors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // UserService의 signup 메서드에 데이터 넘겨 줌
        userService.signup(requestDto);

        // 성공 메시지와 상태 코드 반환
        response.put("message", "회원가입이 성공적으로 이루어졌습니다.");
        response.put("status", HttpStatus.OK.value());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
