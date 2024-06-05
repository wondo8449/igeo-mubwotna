package com.sparta.igeomubwotna.service;

import com.sparta.igeomubwotna.dto.Response;
import com.sparta.igeomubwotna.dto.SigninRequestDto;
import com.sparta.igeomubwotna.dto.SignupRequestDto;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.jwt.JwtUtil;
import com.sparta.igeomubwotna.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ResponseEntity<Response> signup(SignupRequestDto requestDto, BindingResult bindingResult) {
        ResponseEntity<Response> returnError = checkError("회원가입에 실패하였습니다.", bindingResult);

        if (returnError != null) {
            return returnError;
        }

        String userId = requestDto.getUserId();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 userId 중복 확인
        Optional<User> checkUserId = userRepository.findByUserId(userId);
        if (checkUserId.isPresent()) {
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

    public ResponseEntity<Response> signin(@Valid SigninRequestDto requestDto, HttpServletResponse res, BindingResult bindingResult) {
        ResponseEntity<Response> returnError = checkError("로그인에 실패하였습니다.", bindingResult);

        if (returnError != null) {
            return returnError;
        }

        String userId = requestDto.getUserId();
        String password = requestDto.getPassword();

        // 있는 회원인지 확인
        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isPresent()) {
            // 비밀번호 확인(평문, 암호화)
            // password가 일치하지 않으면
            if(!passwordEncoder.matches(password, user.get().getPassword())) {
                // 오류 메시지와 상태 코드 반환
                Response response = new Response(HttpStatus.BAD_REQUEST.value(), "비밀번호가 다릅니다.");

                return ResponseEntity.badRequest().body(response);
            }

            // TODO: JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
            String token = jwtUtil.creatToken(userId); // 토큰 생성
            jwtUtil.addJwtToCookie(token, res); // 쿠키 생성 후 토큰 쿠키에 저장

        } else {
            // 오류 메시지와 상태 코드 반환
            Response response = new Response(HttpStatus.BAD_REQUEST.value(), "아이디가 존재하지 않습니다.");

            return ResponseEntity.badRequest().body(response);
        }

        Response response = new Response(HttpStatus.OK.value(), "로그인에 성공하였습니다.");

        return ResponseEntity.ok().body(response);
    }

    // 클라이언트에서 입력받아 오는 값을 유효성 검사하는 로직
    public ResponseEntity<Response> checkError(String message, BindingResult bindingResult) {
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
            Response response = new Response(HttpStatus.BAD_REQUEST.value(), message);
            return ResponseEntity.badRequest().body(response);
        }

        return null;
    }
}
