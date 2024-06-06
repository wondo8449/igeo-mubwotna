package com.sparta.igeomubwotna.filter;

import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.jwt.JwtUtil;
import com.sparta.igeomubwotna.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthFilter")
@Component
@RequiredArgsConstructor
@Order(1)
public class AuthFilter implements Filter {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(url) && (url.startsWith("/user/signin") || url.startsWith("/user/signup"))) {
            // 회원가입, 로그인 관련 API는 인증 필요없이 요청 진행
            chain.doFilter(request, response); // 다음 필터 이동
        } else {
            // 나머지 API 요청은 인증 처리 진행
            // 토큰 확인
            String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

            if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                // JWT 토큰 substring
                String token = jwtUtil.substringToken(tokenValue);

                // 토큰 검증
                if (!jwtUtil.validateToken(token)) {
                    throw new IllegalArgumentException("토큰 에러");
                }

                // 토큰에서 사용자 정보 가져오기
                Claims info = jwtUtil.getUserInfoFromToken(token);

                // 유저를 userId로 찾아오기
                User user = userRepository.findByUserId(info.getId()).orElseThrow(() ->
                        new NullPointerException("사용자를 찾을 수 없습니다")
                );

                // controller에서 사용할 수 있게 user를 담아줌
                request.setAttribute("user", user);
                chain.doFilter(request, response); // 다음 필터로 이동
            } else {
                throw new IllegalArgumentException("토큰을 찾지 못했습니다.");
            }
        }
    }
}
