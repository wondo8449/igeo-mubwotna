package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.dto.Response;
import com.sparta.igeomubwotna.dto.SigninRequestDto;
import com.sparta.igeomubwotna.dto.SignupRequestDto;
import com.sparta.igeomubwotna.dto.UserProfileDto;
import com.sparta.igeomubwotna.dto.UserUpdateRequestDto;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping("/user/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(Authentication authentication) {
        // 인증 객체에서 사용자 정보를 추출
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserProfileDto userProfile = userService.getUserProfile(userDetails.getUser().getId());
        return ResponseEntity.ok(userProfile);
    }

    @PatchMapping("/user/me")
    public ResponseEntity<Response> updateUserProfile(@RequestBody @Valid UserUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 인증 객체에서 사용자 정보를 추출
        return userService.updateUserProfile(requestDto, userDetails.getUser().getId());
    }
}
