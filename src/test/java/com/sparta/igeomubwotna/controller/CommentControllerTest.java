package com.sparta.igeomubwotna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.igeomubwotna.config.SecurityConfig;
import com.sparta.igeomubwotna.dto.*;
import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.CommentService;
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
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
public class CommentControllerTest {

    private MockMvc mvc;
    private Principal mockPrincipal;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    CommentService commentService;

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
    void saveTest() throws Exception {
        // given
        User user = mockUserSetup();
        Recipe recipe = mockRecipeSetup();
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용");
        Comment comment = new Comment(requestDto, recipe, user);
        CommentResponseDto responseDto = new CommentResponseDto(1L, comment.getContent(), comment.getUser().getUserId(), comment.getCreatedAt(), comment.getLikeCount());

        given(commentService.createComment(any(CommentRequestDto.class), any(Long.class), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(responseDto));

        // when
        mvc.perform(post("/recipe/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding("utf-8")
                        .principal(mockPrincipal))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(responseDto.getContent()))
                .andExpect(jsonPath("$.userId").value(responseDto.getUserId()))
                .andDo(print());
    }

    @Test
    void getTest() throws Exception {
        // given
        Comment comment1 = mockCommentSetup("1");
        Comment comment2 = mockCommentSetup("2");
        List<CommentResponseDto> commentList = new ArrayList<>();
        commentList.add(new CommentResponseDto(1L, comment1.getContent(), comment1.getUser().getUserId(), comment1.getCreatedAt(), comment1.getLikeCount()));
        commentList.add(new CommentResponseDto(2L, comment2.getContent(), comment2.getUser().getUserId(), comment2.getCreatedAt(), comment2.getLikeCount()));

        given(commentService.getComment(1L)).willReturn(commentList);
        // when
        mvc.perform(get("/recipe/1/comment")
                        .principal(mockPrincipal))
                // then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteTest() throws Exception {
        // when
        mockUserSetup();
        mvc.perform(delete("/recipe/1/comment/1")
                        .principal(mockPrincipal)
                )
                // then
                .andExpect(status().isOk())
                .andDo(print());
    }
}
