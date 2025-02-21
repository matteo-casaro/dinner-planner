package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.enums.IngredientType;
import me.personal.dinner_planner.models.Ingredient;
import me.personal.dinner_planner.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("junit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("UnitTest")
public class IngredientRepositoryTest {

    private final IngredientRepository ingredientRepository;

    private final UserRepository userRepository;

    private User testUser;

    @Autowired
    public IngredientRepositoryTest(IngredientRepository ingredientRepository, UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
    }

    private User generateTestUser() {
        return User.builder()
                .email("john_doe@email.org")
                .password("password")
                .build();
    }

    private Ingredient generateTestIngredient() {
        return Ingredient.builder()
                .name("Test Ingredient")
                .type(IngredientType.FRUIT)
                .user(testUser)
                .createdAt(LocalDateTime.now())
                .unit("g")
                .build();
    }

    @BeforeEach
    void createTestEntities() {
        testUser = userRepository.saveAndFlush(generateTestUser());
    }

    @Test
    @Order(1)
    void shouldSaveIngredient() {
        Ingredient savedIngredient = ingredientRepository.saveAndFlush(generateTestIngredient());
        Assertions.assertNotNull(savedIngredient.getId());
        Assertions.assertEquals("Test Ingredient", savedIngredient.getName());
        Assertions.assertEquals(IngredientType.FRUIT, savedIngredient.getType());
        Assertions.assertEquals("g", savedIngredient.getUnit());
        Assertions.assertNotNull(savedIngredient.getCreatedAt());
        Assertions.assertEquals(testUser.getId(), savedIngredient.getUser().getId());
    }

    @Test
    @Order(2)
    void shouldFindIngredientsByUserId() {
        ingredientRepository.saveAndFlush(generateTestIngredient());
        List<Ingredient> ingredients = ingredientRepository.findByUserId(testUser.getId());
        Assertions.assertFalse(ingredients.isEmpty());
        Ingredient savedIngredient = ingredients.getFirst();
        Assertions.assertEquals("Test Ingredient", savedIngredient.getName());
        Assertions.assertEquals(IngredientType.FRUIT, savedIngredient.getType());
        Assertions.assertEquals("g", savedIngredient.getUnit());
        Assertions.assertNotNull(savedIngredient.getCreatedAt());
        Assertions.assertEquals(testUser.getId(), savedIngredient.getUser().getId());
    }

    @Test
    @Order(3)
    void shouldDeleteIngredient() {
        ingredientRepository.saveAndFlush(generateTestIngredient());
        List<Ingredient> ingredients = ingredientRepository.findByUserId(testUser.getId());
        Assertions.assertFalse(ingredients.isEmpty());
        ingredientRepository.delete(ingredients.getFirst());
        List<Ingredient> ingredientsAfterDelete = ingredientRepository.findByUserId(testUser.getId());
        Assertions.assertTrue(ingredientsAfterDelete.isEmpty());
    }

}
