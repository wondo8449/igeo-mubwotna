package com.sparta.igeomubwotna.controller;

import com.sparta.igeomubwotna.dto.Response;
import com.sparta.igeomubwotna.dto.SignupRequestDto;
import com.sparta.igeomubwotna.dto.UserProfileDto;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<Response> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // 유효성 검사 예외 처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // 유효성 검사에 오류가 있으면
        if (fieldErrors.size() > 0) {
            // 모든 필드 오류에 대해 로그 기록 및 오류 메시지 수집
            List<String> errorMessages = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String errorMessage = fieldError.getField() + " 필드: " + fieldError.getDefaultMessage();
                log.error(errorMessage);
                errorMessages.add(errorMessage);
            }
            // 오류 메시지와 상태 코드 반환
            Response response = new Response(HttpStatus.BAD_REQUEST.value(), "회원가입에 실패하였습니다.", errorMessages);
            return ResponseEntity.badRequest().body(response);
        }

        // UserService의 signup 메서드에 데이터 넘겨 줌
        return userService.signup(requestDto);
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserProfileDto userProfile = userService.getUserProfile(userDetails.getUser().getId());
        return ResponseEntity.ok(userProfile);
    }
}
