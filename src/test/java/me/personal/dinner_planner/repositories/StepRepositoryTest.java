package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.models.Recipe;
import me.personal.dinner_planner.models.Step;
import me.personal.dinner_planner.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("IntegrationTest")
class StepRepositoryTest {

    private final StepRepository stepRepository;
    
    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private User testUser;
    
    private Recipe testRecipe;

    @Autowired
    public StepRepositoryTest(StepRepository stepRepository, RecipeRepository recipeRepository, UserRepository userRepository) {
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
    
    private Step generateTestStep(int orderNumber) {
        return Step.builder()
                .description("Test Step")
                .orderNumber(orderNumber)
                .title("Test Title " + orderNumber)
                .createdAt(LocalDateTime.now())
                .recipe(testRecipe)
                .build();
    }

    @BeforeEach
    void createTestEntities() {
        testUser = userRepository.saveAndFlush(generateTestUser());
        testRecipe = recipeRepository.saveAndFlush(generateTestRecipe());
    }

    @Test
    @Order(1)
    void shouldSaveStep() {
        Step savedStep = stepRepository.saveAndFlush(generateTestStep(1));
        Assertions.assertNotNull(savedStep.getId());
        Assertions.assertEquals("Test Step", savedStep.getDescription());
        Assertions.assertEquals(1, savedStep.getOrderNumber());
        Assertions.assertEquals("Test Title 1", savedStep.getTitle());
        Assertions.assertNotNull(savedStep.getCreatedAt());
        Assertions.assertEquals(testRecipe.getId(), savedStep.getRecipe().getId());
        Assertions.assertEquals(testUser.getId(), savedStep.getRecipe().getUser().getId());
    }

    @Test
    @Order(2)
    void shouldFindStepsByRecipeIdAndOrderThem() {
        stepRepository.saveAndFlush(generateTestStep(1));
        stepRepository.saveAndFlush(generateTestStep(2));
        List<Step> steps = stepRepository.findByRecipeIdOrderByOrderNumber(testRecipe.getId());
        Assertions.assertFalse(steps.isEmpty());
        Step firstStep = steps.getFirst();
        Step secondStep = steps.get(1);

        Assertions.assertNotNull(firstStep.getId());
        Assertions.assertEquals("Test Step", firstStep.getDescription());
        Assertions.assertEquals(1, firstStep.getOrderNumber());
        Assertions.assertEquals("Test Title 1", firstStep.getTitle());
        Assertions.assertNotNull(firstStep.getCreatedAt());
        Assertions.assertEquals(testRecipe.getId(), firstStep.getRecipe().getId());
        Assertions.assertEquals(testUser.getId(), firstStep.getRecipe().getUser().getId());

        Assertions.assertNotNull(secondStep.getId());
        Assertions.assertEquals("Test Step", secondStep.getDescription());
        Assertions.assertEquals(2, secondStep.getOrderNumber());
        Assertions.assertEquals("Test Title 2", secondStep.getTitle());
        Assertions.assertNotNull(secondStep.getCreatedAt());
        Assertions.assertEquals(testRecipe.getId(), secondStep.getRecipe().getId());
        Assertions.assertEquals(testUser.getId(), secondStep.getRecipe().getUser().getId());
    }

    @Test
    @Order(3)
    void shouldDeleteStep() {
        stepRepository.saveAndFlush(generateTestStep(1));
        stepRepository.saveAndFlush(generateTestStep(2));
        List<Step> steps = stepRepository.findByRecipeIdOrderByOrderNumber(testRecipe.getId());
        Assertions.assertFalse(steps.isEmpty());
        Assertions.assertEquals(2, steps.size());
        stepRepository.delete(steps.get(1));
        List<Step> recipesAfterDeleteSecondStep = stepRepository.findByRecipeIdOrderByOrderNumber(testRecipe.getId());
        Assertions.assertFalse(recipesAfterDeleteSecondStep.isEmpty());
        Assertions.assertEquals(1, recipesAfterDeleteSecondStep.size());
        Assertions.assertEquals("Test Title 1", recipesAfterDeleteSecondStep.getFirst().getTitle());
        stepRepository.delete(recipesAfterDeleteSecondStep.getFirst());
        List<Step> recipesAfterDeleteLastStep = stepRepository.findByRecipeIdOrderByOrderNumber(testRecipe.getId());
        Assertions.assertTrue(recipesAfterDeleteLastStep.isEmpty());
    }

    @Test
    @Order(4)
    void shouldFailOnSameRecipeAndOrderNumber() {
        stepRepository.saveAndFlush(generateTestStep(1));
        Assertions.assertThrows(Exception.class, () -> stepRepository.saveAndFlush(generateTestStep(1)));
    }

    @Test
    @Order(5)
    void shouldNotFindStepsByRecipeId() {
        List<Step> steps = stepRepository.findByRecipeIdOrderByOrderNumber(testRecipe.getId());
        Assertions.assertTrue(steps.isEmpty());
    }

}
