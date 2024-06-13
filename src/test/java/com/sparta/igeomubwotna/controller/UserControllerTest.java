package com.sparta.igeomubwotna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.igeomubwotna.config.SecurityConfig;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.filter.JwtAuthenticationFilter;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {UserController.class, JwtAuthenticationFilter.class},
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

    private void mockUserSetup() {
        // Mock 테스트 유저 생성
        String userId = "kyc123123";
        String password = "kyc123123@";
        String name = "김예찬";
        String email = "kyc@email.com";
        String description = "한줄 소개";
        User testUser = new User(userId, password, name, email, description);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    void loginTest() throws Exception {
        // given
        this.mockUserSetup();

        // when - then
        mvc.perform(get("/user/signin").with(user("kyc123123").password("kyc123123@")))
                .andExpect(authenticated())
                .andDo(print());
    }

    @Test
    void signupTest() throws Exception {
        // given
        Map<String, String> signup = new HashMap<>();
        signup.put("userId", "kyc123123");
        signup.put("password", "kyc123123@");
        signup.put("name", "김예찬");
        signup.put("email", "kyc@email.com");
        signup.put("description", "한줄 소개");

        // when - then
        mvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup))
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    void meTest() throws Exception {
        // given
        this.mockUserSetup();
        // when - then
        mvc.perform(get("/user/me")
                        .principal(mockPrincipal))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    void meUpdateTest() throws Exception {
        // given
        this.mockUserSetup();
        Map<String, String> update = new HashMap<>();
        update.put("name", "김예찬 수정");
        update.put("description", "한줄 소개 수정");

        // when - then
        mvc.perform(patch("/user/me")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    void logoutTest() throws Exception {
        // given
        this.mockUserSetup();

        // when - then
        mvc.perform(post("/user/logout")
                        .principal(mockPrincipal))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    void withdrawTest() throws Exception {
        // given
        this.mockUserSetup();
        Map<String, String> withdraw = new HashMap<>();
        withdraw.put("password", "kyc123123@");

        // when - then
        // when - then
        mvc.perform(patch("/user/withdraw")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}