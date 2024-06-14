package com.sparta.igeomubwotna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.igeomubwotna.config.SecurityConfig;
import com.sparta.igeomubwotna.dto.*;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
class UserControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private User mockUserSetup(String num) {
        // Mock 테스트 유저 생성
        Long id = Long.valueOf(num);
        String userId = "kyc123123" + num;
        String password = "kyc123123@"+ num;
        String name = "김예찬";
        String email = num + "kyc@email.com";
        String description = "한줄 소개";
        User testUser = new User(id, userId, password, name, email, description);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
        return testUser;
    }

    @Test
    void signupTest() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto("kyc1234", "kyc1234@", "김예찬", "kyc@email.com", "한줄 소개");
        Response response = new Response(HttpStatus.CREATED.value(), "회원가입에 성공하였습니다.");

        given(userService.signup(any(SignupRequestDto.class),any(BindingResult.class))).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(response));

        // when
        mvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding("utf-8")
                )
        // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(response.getMessage()))
                .andDo(print());
    }

    @Test
    void meTest() throws Exception {
        // given
        User user = this.mockUserSetup("1");
        UserProfileDto userProfileDto = new UserProfileDto(user.getUserId(), user.getName(), user.getDescription(), user.getEmail());

        given(userService.getUserProfile(any(Long.class))).willReturn(userProfileDto);

        // when
        mvc.perform(get("/user/me")
                        .principal(mockPrincipal)
                        .characterEncoding("utf-8"))
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userProfileDto.getUserId()))
                .andExpect(jsonPath("$.name").value(userProfileDto.getName()))
                .andExpect(jsonPath("$.description").value(userProfileDto.getDescription()))
                .andExpect(jsonPath("$.email").value(userProfileDto.getEmail()))
                .andDo(print());
    }

    @Test
    void meUpdateTest() throws Exception {
        // given
        this.mockUserSetup("1");
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("김예찬 수정", "한줄 소개 수정");
        Long userNumber = 1L;
        Response response = new Response(HttpStatus.OK.value(), "회원정보 수정 성공!");

        given(userService.updateUserProfile(any(UserUpdateRequestDto.class),any(Long.class))).willReturn(ResponseEntity.ok().body(response));

        // when
        mvc.perform(patch("/user/me")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding("utf-8")
                )
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(response.getMessage()))
                .andDo(print());
    }

    @Test
    void logoutTest() throws Exception {
        // given
        this.mockUserSetup("1");
        Long userNumber = 1L;
        Response response = new Response(HttpStatus.OK.value(), "로그아웃 성공!");

        given(userService.logout(1L)).willReturn(ResponseEntity.ok().body(response));

        // when
        mvc.perform(post("/user/logout")
                        .principal(mockPrincipal)
                        .characterEncoding("utf-8"))

        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(response.getMessage()))
                .andDo(print());
    }

    @Test
    void withdrawTest() throws Exception {
        // given
        this.mockUserSetup("1");
        PasswordDto passwordDto = new PasswordDto("kyc123123@1");
        Response response = new Response(HttpStatus.OK.value(), "회원탈퇴 성공!");

        given(userService.withdrawUser(any(PasswordDto.class),any(Long.class))).willReturn(ResponseEntity.ok().body(response));

        // when
        mvc.perform(patch("/user/withdraw")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto))
                        .characterEncoding("utf-8")
                )
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(response.getMessage()))
                .andDo(print());
    }
}