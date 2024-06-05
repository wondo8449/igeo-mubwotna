package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.dto.Response;
import com.sparta.igeomubwotna.dto.SigninRequestDto;
import com.sparta.igeomubwotna.dto.SignupRequestDto;
import com.sparta.igeomubwotna.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<Response> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // UserService의 signup 메서드에 데이터 넘겨 줌
        return userService.signup(requestDto, bindingResult);
    }

    @PostMapping("/user/login")
    public ResponseEntity<Response> signin(@RequestBody @Valid SigninRequestDto requestDto, HttpServletResponse res, BindingResult bindingResult) {
        return userService.signin(requestDto, res, bindingResult);
    }

}
