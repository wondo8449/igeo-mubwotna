package com.sparta.igeomubwotna.dto;

import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    User user;
    Recipe recipe;
    Comment comment;

    @BeforeEach
    void setUp() {
        // given
        String userId = "kyc123123";
        String password = "kyc123123@";
        String name = "김예찬";
        String email = "kyc@email.com";
        String description = "한줄 소개";

        user = new User(userId, password, name, email, description);

        String retitle = "레시피 제목";
        String recontent = "레시피 내용";

        recipe = new Recipe(user, retitle, recontent);

        String coContent = "댓글 내용";

        comment = new Comment(new CommentRequestDto(coContent), recipe, user);
    }

    @Nested
    @DisplayName("유저 Entity 테스트")
    class UserTest {
        @Test
        void userUpdateTest() {
            // when
            user.updateName("김예찬 수정");
            user.updatePassword("kyc123123@@");
            user.updateDescription("한줄 소개 수정");
            user.updateRefreshToken("RefreshToken");

            // then
            assertEquals("김예찬 수정", user.getName(), "수정에 오류가 있습니다!");
            assertEquals("kyc123123@@", user.getPassword(), "수정에 오류가 있습니다!");
            assertEquals("한줄 소개 수정", user.getDescription(), "수정에 오류가 있습니다!");
            assertEquals("RefreshToken", user.getRefreshToken(), "수정에 오류가 있습니다!");
        }

        @Test
        void userWithdarwnTest() {
            // when
            user.withdraw();

            //then
            assertTrue(user.isWithdrawn());
        }
    }

    @Nested
    @DisplayName("레시피 Entity 테스트")
    class RecipeTest {
        @Test
        void recipeUpdateTest() {
            // given
            String title = "레시피 수정 제목";
            String content = "레시피 수정 내용";

            // when
            recipe.update(new RecipeRequestDto(title, content));

            // then
            assertEquals("레시피 수정 제목", recipe.getTitle(), "수정에 오류가 있습니다!");
            assertEquals("레시피 수정 내용", recipe.getContent(), "수정에 오류가 있습니다!");
        }

        @Test
        void recipeAddMinusTest() {
            // when
            recipe.addLike();

            // then
            assertEquals(1, recipe.getRecipeLikes(), "좋아요 기능의 문제가 있습니다.");

            // when
            recipe.minusLike();

            // then
            assertEquals(0, recipe.getRecipeLikes(), "좋아요 기능의 문제가 있습니다.");
        }
    }
    @Nested
    @DisplayName("댓글 Entity 테스트")
    class CommentTest {
        @Test
        void commentUpdateTest() {
            // given
            String content = "댓글 수정 내용";

            // when
            comment.update(content);

            // then
            assertEquals("댓글 수정 내용", comment.getContent(), "수정에 오류가 있습니다!");
        }

        @Test
        void commentAddMinusTest() {
            // when
            comment.addLike();

            // then
            assertEquals(1, comment.getLikeCount(), "좋아요 기능의 문제가 있습니다.");

            // when
            comment.minusLike();

            // then
            assertEquals(0, comment.getLikeCount(), "좋아요 기능의 문제가 있습니다.");
        }
    }
}
