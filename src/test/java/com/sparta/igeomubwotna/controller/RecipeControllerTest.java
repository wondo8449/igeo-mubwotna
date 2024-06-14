package com.sparta.igeomubwotna.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.igeomubwotna.config.SecurityConfig;
import com.sparta.igeomubwotna.dto.RecipeRequestDto;
import com.sparta.igeomubwotna.dto.RecipeResponseDto;
import com.sparta.igeomubwotna.dto.Response;
import com.sparta.igeomubwotna.dto.UserUpdateRequestDto;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.RecipeRepository;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        controllers = {RecipeController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
public class RecipeControllerTest {
    private MockMvc mvc;
    private Principal mockPrincipal;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    RecipeService recipeService;

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

    private Recipe mockRecipeSetup(String num) {
        User user = mockUserSetup();
        String title = "레시피 제목" + num;
        String content = "레시피 내용" + num;
        Recipe recipe = new Recipe(user, title, content);
        return recipe;
    }

    @Test
    void saveTest() throws Exception {
        // given
        User user = mockUserSetup();
        RecipeRequestDto requestDto = new RecipeRequestDto("레시피 제목", "레시피 내용");
        RecipeResponseDto responseDto = new RecipeResponseDto(new Recipe(requestDto, user));

        given(recipeService.saveRecipe(any(RecipeRequestDto.class), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(responseDto));

        // when
        mvc.perform(post("/recipe/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding("utf-8")
                        .principal(mockPrincipal))
        // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.content").value(responseDto.getContent()))
                .andExpect(jsonPath("$.userId").value(responseDto.getUserId()))
                .andDo(print());
    }

    @Test
    void getTest() throws Exception {
        // given
        User user = mockUserSetup();
        Recipe recipe = new Recipe(user, "레시피 제목", "레시피 내용");
        given(recipeService.getRecipe(1L)).willReturn(ResponseEntity.status(HttpStatus.OK).body(recipe));
        // when
        mvc.perform(get("/recipe/1"))
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$.content").value(recipe.getContent()))
                .andExpect(jsonPath("$.user.userId").value(recipe.getUser().getUserId()))
                .andDo(print());
    }

    @Test
    void editTest() throws Exception {
        // given
        User user = mockUserSetup();
        Recipe recipe = mockRecipeSetup("1");
        RecipeRequestDto requestDto = new RecipeRequestDto("수정된 레시피 제목", "수정된 레시피 내용");
        recipe.update(requestDto);
        RecipeResponseDto responseDto = new RecipeResponseDto(recipe);

        given(recipeService.editRecipe(any(Long.class), any(RecipeRequestDto.class), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.OK).body(responseDto));
        // when
        mvc.perform(patch("/recipe/1")
                        .characterEncoding("utf-8")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                )
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$.content").value(recipe.getContent()))
                .andExpect(jsonPath("$.userId").value(recipe.getUser().getUserId()))
                .andDo(print());
    }

    @Test
    void deleteTest() throws Exception {
        // given
        User user = mockUserSetup();
        Recipe recipe = mockRecipeSetup("1");
        Response response = new Response(HttpStatus.OK.value(),recipe.getId() + "번 게시물 삭제 완료");

        given(recipeService.deleteRecipe(any(Long.class), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.OK).body(response));
        // when
        mvc.perform(delete("/recipe/0")
                        .principal(mockPrincipal)
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(response.getMessage()))
                .andDo(print());
    }

    @Test
    void getAllTest() throws Exception {
        // given
        Recipe recipe1 = mockRecipeSetup("1");
        Recipe recipe2 = mockRecipeSetup("2");
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        recipeList.add(recipe2);

        given(recipeService.getAllRecipe(any(Integer.class), any(String.class))).willReturn(ResponseEntity.status(HttpStatus.OK).body(recipeList));

        // when
        mvc.perform(get("/recipe/?page=1")
                        .principal(mockPrincipal)
                )
                // then
                .andExpect(status().isOk())
                .andDo(print());
    }
}
