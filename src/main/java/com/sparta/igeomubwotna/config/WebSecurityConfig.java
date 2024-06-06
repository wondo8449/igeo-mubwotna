package com.sparta.igeomubwotna.config;

import com.sparta.igeomubwotna.filter.JwtAuthenticationFilter;
import com.sparta.igeomubwotna.filter.JwtAuthorizationFilter;
import com.sparta.igeomubwotna.jwt.JwtUtil;
import com.sparta.igeomubwotna.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;  //사용자 정보를 로드하는 서비스
    private final AuthenticationConfiguration authenticationConfiguration;  //인증 구성을 위한 클래스.

    @Bean
    public PasswordEncoder passwordEncoder() {  //비밀번호를 암호화하기 위한 PasswordEncoder를 빈으로 정의
        return new BCryptPasswordEncoder();  //여기서는 BCryptPasswordEncoder를 사용
    }

    @Bean
    // 인증 관리자를 빈으로 정의
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();  //authenticationConfiguration을 이용하여 AuthenticationManager를 반환
    }

    @Bean
    // JWT 인증 필터를 빈으로 정의
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        // 이 필터는 JWT를 사용하여 인증을 처리하며, 인증 관리자를 설정
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    // JWT 인가(권한 부여) 필터를 빈으로 정의
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        // 이 필터는 JWT를 사용하여 권한 부여를 처리
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());  //CSRF 보호를 비활성화

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 요청 권한 설정: 특정 URL 패턴에 대한 접근 권한을 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/user/signup").permitAll() // 회원가입 요청 모두 접근 허가
                        .requestMatchers("/user/signin").permitAll() // 로그인 요청 모두 접근 허가
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 필터 관리: JWT 인증 필터와 권한 부여 필터를 필터 체인에 추가
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // SecurityFilterChain 객체를 생성하고 반환
        return http.build();
    }
}