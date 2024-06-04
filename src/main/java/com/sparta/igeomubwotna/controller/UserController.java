package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.dto.SignupRequestDto;
import com.sparta.igeomubwotna.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/user/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {

        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원가입이 성공적으로 이루어졌습니다.");
        response.put("status", HttpStatus.OK.value());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
