package com.sparta.igeomubwotna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.igeomubwotna.config.SecurityConfig;
import com.sparta.igeomubwotna.dto.CommentRequestDto;
import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.CommentService;
import com.sparta.igeomubwotna.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {LikeController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
public class LikeControllerTest {

    private MockMvc mvc;
    private Principal mockPrincipal;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    LikeService likeService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private User mockUserSetup() {
        // Mock 테스트 유저 생성
        Long id = 1L;
        String userId = "kyc123123";
        String password = "kyc123123@";
        String name = "김예찬";
        String email = "kyc@email.com";
        String description = "한줄 소개";
        User testUser = new User(id, userId, password, name, email, description);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
        return testUser;
    }

    private Recipe mockRecipeSetup() {
        User user = mockUserSetup();
        String title = "레시피 제목";
        String content = "레시피 내용";
        Recipe recipe = new Recipe(user, title, content);
        return recipe;
    }

    private Comment mockCommentSetup(String num) {
        User user = mockUserSetup();
        Recipe recipe = mockRecipeSetup();
        CommentRequestDto requestDto1 = new CommentRequestDto("댓글 1");
        Comment comment = new Comment(new CommentRequestDto("댓글 내용" + num), recipe, user);
        return comment;
    }

    @Test
    void addRecipeLikeTest() throws Exception {
        // given
        mockUserSetup();
        String msg = "좋아요 성공!";

        given(likeService.addRecipeLike(any(Long.class), any(User.class))).willReturn(ResponseEntity.status(200).body(msg));
        // when
        mvc.perform(post("/recipe/1/like")
                        .principal(mockPrincipal))
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(msg))
                .andDo(print());
    }

    @Test
    void removeRecipeLikeTest() throws Exception {
        // given
        mockUserSetup();
        String msg = "좋아요 취소 성공!";

        given(likeService.removeRecipeLike(any(Long.class), any(User.class))).willReturn(ResponseEntity.status(200).body(msg));
        // when
        mvc.perform(delete("/recipe/1/like")
                        .principal(mockPrincipal))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(msg))
                .andDo(print());
    }

    @Test
    void addCommentLikeTest() throws Exception {
        // given
        mockUserSetup();
        String msg = "좋아요 성공!";

        given(likeService.addCommentLike(any(Long.class), any(User.class))).willReturn(ResponseEntity.status(200).body(msg));
        // when
        mvc.perform(post("/comment/0/like")
                        .principal(mockPrincipal))
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(msg))
                .andDo(print());
    }

    @Test
    void removeCommentLikeTest() throws Exception {
        // given
        mockUserSetup();
        String msg = "좋아요 취소 성공!";

        given(likeService.removeCommentLike(any(Long.class), any(User.class))).willReturn(ResponseEntity.status(200).body(msg));
        // when
        mvc.perform(delete("/comment/1/like")
                        .principal(mockPrincipal))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(msg))
                .andDo(print());
    }
}
