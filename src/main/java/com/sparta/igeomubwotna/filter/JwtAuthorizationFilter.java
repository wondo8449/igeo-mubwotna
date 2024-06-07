package com.sparta.igeomubwotna.filter;

import com.sparta.igeomubwotna.jwt.JwtUtil;
import com.sparta.igeomubwotna.security.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String url = req.getRequestURI();

        if(StringUtils.hasText(url) && (url.equals("/user/signin") || url.equals("/user/signup"))) {
            filterChain.doFilter(req, res);
            return; // 필터 체인을 빠져나갑니다.
        }

        // HTTP 요청에서 JWT 토큰 추출
        String tokenValue = jwtUtil.substringToken(jwtUtil.getTokenFromRequest(req));

        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 유효성 검증
            if (!jwtUtil.validateToken(tokenValue)) {
                // 유효하지 않은 토큰이면 에러 로깅 후 종료
                log.error("Token Error");
                return;
            }
            // JWT 토큰으로부터 사용자 정보(Claims) 추출
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                // 사용자 인증 처리
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                // 인증 처리 중 예외 발생 시 에러 로깅 후 종료
                log.error(e.getMessage());
                return;
            }
        }

        // 다음 필터(또는 서블릿)로 요청 전달
        filterChain.doFilter(req, res);
    }

    // 사용자 인증 처리
    public void setAuthentication(String username) {
        // 빈 SecurityContext 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 사용자의 인증 객체 생성
        Authentication authentication = createAuthentication(username);
        // SecurityContext에 인증 객체 설정
        context.setAuthentication(authentication);

        // SecurityContext를 SecurityContextHolder에 설정
        SecurityContextHolder.setContext(context);
    }

    // 사용자의 인증 객체 생성
    private Authentication createAuthentication(String username) {
        // 사용자 정보 조회
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // UserDetails를 사용하여 인증 토큰 생성
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}