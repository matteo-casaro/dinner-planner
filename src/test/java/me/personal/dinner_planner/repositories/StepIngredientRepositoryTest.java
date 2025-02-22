package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.enums.IngredientType;
import me.personal.dinner_planner.models.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("IntegrationTest")
class StepIngredientRepositoryTest {

    private final StepIngredientRepository stepIngredientRepository;

    private final IngredientRepository ingredientRepository;

    private final StepRepository stepRepository;

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private User testUser;

    private Recipe testRecipe;

    private Step testStep;

    private Ingredient testIngredient;

    @Autowired
    public StepIngredientRepositoryTest(
            StepIngredientRepository stepIngredientRepository,
            IngredientRepository ingredientRepository,
            StepRepository stepRepository,
            RecipeRepository recipeRepository,
            UserRepository userRepository) {
        this.stepIngredientRepository = stepIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.stepRepository = stepRepository;
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
    
    private Step generateTestStep() {
        return Step.builder()
                .description("Test Step")
                .orderNumber(1)
                .title("Test Title")
                .createdAt(LocalDateTime.now())
                .recipe(testRecipe)
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

    private StepIngredient generateTestStepIngredient() {
        return StepIngredient.builder()
                .ingredient(testIngredient)
                .step(testStep)
                .quantity(new BigDecimal(100))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    void createTestEntities() {
        testUser = userRepository.saveAndFlush(generateTestUser());
        testIngredient = ingredientRepository.saveAndFlush(generateTestIngredient());
        testRecipe = recipeRepository.saveAndFlush(generateTestRecipe());
        testStep = stepRepository.saveAndFlush(generateTestStep());
    }

    @Test
    @Order(1)
    void shouldSaveStepIngredient() {
        StepIngredient saved = stepIngredientRepository.saveAndFlush(generateTestStepIngredient());
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Test Step", saved.getStep().getDescription());
        Assertions.assertEquals("Test Title", saved.getStep().getTitle());
        Assertions.assertEquals(1, saved.getStep().getOrderNumber());
        Assertions.assertEquals("Test Recipe", saved.getStep().getRecipe().getName());
        Assertions.assertEquals("Test Description", saved.getStep().getRecipe().getDescription());
        Assertions.assertEquals(testUser.getId(), saved.getStep().getRecipe().getUser().getId());
        Assertions.assertEquals("Test Ingredient", saved.getIngredient().getName());
        Assertions.assertEquals(IngredientType.FRUIT, saved.getIngredient().getType());
        Assertions.assertEquals(testUser.getId(), saved.getIngredient().getUser().getId());
        Assertions.assertEquals(100, saved.getQuantity().intValue());
        Assertions.assertEquals("g", saved.getIngredient().getUnit());
    }

    @Test
    @Order(2)
    void shouldFindIngredientsByStepId() {
        stepIngredientRepository.saveAndFlush(generateTestStepIngredient());
        List<StepIngredient> stepIngredients = stepIngredientRepository.findByStepId(testStep.getId());
        Assertions.assertFalse(stepIngredients.isEmpty());
        Assertions.assertEquals(1, stepIngredients.size());
        StepIngredient stepIngredient = stepIngredients.getFirst();
        Assertions.assertEquals("Test Step", stepIngredient.getStep().getDescription());
        Assertions.assertEquals("Test Title", stepIngredient.getStep().getTitle());
        Assertions.assertEquals(1, stepIngredient.getStep().getOrderNumber());
        Assertions.assertEquals("Test Recipe", stepIngredient.getStep().getRecipe().getName());
        Assertions.assertEquals("Test Description", stepIngredient.getStep().getRecipe().getDescription());
        Assertions.assertEquals(testUser.getId(), stepIngredient.getStep().getRecipe().getUser().getId());
        Assertions.assertEquals("Test Ingredient", stepIngredient.getIngredient().getName());
        Assertions.assertEquals(IngredientType.FRUIT, stepIngredient.getIngredient().getType());
        Assertions.assertEquals(testUser.getId(), stepIngredient.getIngredient().getUser().getId());
        Assertions.assertEquals(100, stepIngredient.getQuantity().intValue());
        Assertions.assertEquals("g", stepIngredient.getIngredient().getUnit());
    }

}
