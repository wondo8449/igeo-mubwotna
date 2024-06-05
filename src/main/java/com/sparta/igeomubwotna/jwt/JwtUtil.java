package com.sparta.igeomubwotna.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

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

    // JWT 생성
    // TODO: 나중에 user에 상태가 필요하면 말씀해 주세요!
    public String creatToken(String userId) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userId) // 유저 식별 값
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
//                        .claim() // 상태
    }

    // JWT Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cokkie애는 공백 불가 -> %20로 변경

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // 이름 값
            cookie.setPath("/");

            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }

}