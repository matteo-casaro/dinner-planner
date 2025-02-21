package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.models.Recipe;
import me.personal.dinner_planner.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("junit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("UnitTest")
public class RecipeRepositoryTest {

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private User testUser;

    @Autowired
    public RecipeRepositoryTest(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    private User generateTestUser() {
        return User.builder()
                .email("john_doe@email.org")
                .password("password")
                .build();
    }

    private Recipe generateTestRecipe() {
        return Recipe.builder()
                .name("Test Recipe")
                .description("Test Description")
                .user(testUser)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    void createTestEntities() {
        testUser = userRepository.saveAndFlush(generateTestUser());
    }

    @Test
    @Order(1)
    void shouldSaveRecipe() {
        Recipe savedRecipe = recipeRepository.saveAndFlush(generateTestRecipe());
        Assertions.assertNotNull(savedRecipe.getId());
        Assertions.assertEquals("Test Recipe", savedRecipe.getName());
        Assertions.assertEquals("Test Description", savedRecipe.getDescription());
        Assertions.assertNotNull(savedRecipe.getCreatedAt());
        Assertions.assertEquals(testUser.getId(), savedRecipe.getUser().getId());
    }

    @Test
    @Order(2)
    void shouldFindRecipesByUserId() {
        recipeRepository.saveAndFlush(generateTestRecipe());
        List<Recipe> recipes = recipeRepository.findByUserId(testUser.getId());
        Assertions.assertFalse(recipes.isEmpty());
        Recipe savedRecipe = recipes.getFirst();
        Assertions.assertNotNull(savedRecipe.getId());
        Assertions.assertEquals("Test Recipe", savedRecipe.getName());
        Assertions.assertEquals("Test Description", savedRecipe.getDescription());
        Assertions.assertNotNull(savedRecipe.getCreatedAt());
        Assertions.assertEquals(testUser.getId(), savedRecipe.getUser().getId());
    }

    @Test
    @Order(3)
    void shouldDeleteRecipe() {
        recipeRepository.saveAndFlush(generateTestRecipe());
        List<Recipe> recipes = recipeRepository.findByUserId(testUser.getId());
        Assertions.assertFalse(recipes.isEmpty());
        recipeRepository.delete(recipes.getFirst());
        List<Recipe> recipesAfterDelete = recipeRepository.findByUserId(testUser.getId());
        Assertions.assertTrue(recipesAfterDelete.isEmpty());
    }

    @Test
    @Order(4)
    void shouldNotfindRecipesByUserId() {
        List<Recipe> recipes = recipeRepository.findByUserId(UUID.randomUUID());
        Assertions.assertTrue(recipes.isEmpty());
    }

}
