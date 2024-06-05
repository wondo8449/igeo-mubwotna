package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.dto.Response;
import com.sparta.igeomubwotna.dto.SignupRequestDto;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Response> signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 userId 중복 확인
        Optional<User> checkUsername = userRepository.findByUserId(userId);
        if (checkUsername.isPresent()) {
            // 오류 메시지와 상태 코드 반환
            Response response = new Response(HttpStatus.BAD_REQUEST.value(), "중복된 아이디가 존재합니다.");

            return ResponseEntity.badRequest().body(response);
        }

        // email 중복 확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            // 오류 메시지와 상태 코드 반환
            Response response = new Response(HttpStatus.BAD_REQUEST.value(), "중복된 이메일이 존재합니다.");

            return ResponseEntity.badRequest().body(response);
        }

        // 사용자 등록
        User user = new User(userId, password, requestDto.getName(), requestDto.getEmail(), requestDto.getDescription());
        userRepository.save(user);

        // 성공 메시지와 상태 코드 반환
        Response response = new Response(HttpStatus.OK.value(), "회원가입에 성공하였습니다.");
        return ResponseEntity.ok(response);
    }

}
