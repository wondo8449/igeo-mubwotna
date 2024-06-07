package com.sparta.igeomubwotna.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JWT 관련 로그")
@Component
public class JwtUtil {
    // Header KEY 값 (이름)
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 상태 값의 KEY (이름)
    public static final String AUTHORIZATION_KEY = "status";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; // 30분
    private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 2주

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 딱 한 번만 받아오면 되는 값을 사용할 때마다 요청을 새로고침하는 오류를 방지하기 위해
    @PostConstruct
    public void init() {
        // Base64로 디코딩
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // AccessToken 생성
    // TODO: 나중에 user에 상태가 필요하면 말씀해 주세요!
    public String createAccessToken(String userId) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userId) // 유저 식별 값
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
//                        .claim() // 상태
    }

    // RefreshToken 생성
    public String createRefreshToken(String userId) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 요청 쿠키에서 토큰 추출
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        // 쿠키가 존재하면
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 쿠키 이름이 AUTHORIZATION_HEADER와 일치하는 경우
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        // 쿠키 값을 URL 디코딩하여 반환 (UTF-8 인코딩 사용)
                        // 코딩된 쿠키 값을 다시 원래 값으로 되돌림
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}