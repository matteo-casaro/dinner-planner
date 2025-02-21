package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.enums.DayOfWeek;
import me.personal.dinner_planner.enums.MealType;
import me.personal.dinner_planner.models.MealSchedule;
import me.personal.dinner_planner.models.Recipe;
import me.personal.dinner_planner.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("IntegrationTest")
class MealScheduleRepositoryTest {

    private final MealScheduleRepository mealScheduleRepository;

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private User testUser;

    private Recipe testRecipe;

    @Autowired
    public MealScheduleRepositoryTest(MealScheduleRepository mealScheduleRepository, RecipeRepository recipeRepository, UserRepository userRepository) {
        this.mealScheduleRepository = mealScheduleRepository;
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
    
    private MealSchedule generateTestSchedule(DayOfWeek dayOfWeek, MealType mealType) {
        return MealSchedule.builder()
                .createdAt(LocalDateTime.now())
                .recipe(testRecipe)
                .user(testUser)
                .selectedDay(dayOfWeek)
                .mealType(mealType)
                .build();
    }

    @BeforeEach
    void createTestEntities() {
        testUser = userRepository.saveAndFlush(generateTestUser());
        testRecipe = recipeRepository.saveAndFlush(generateTestRecipe());
    }

    @Test
    @Order(1)
    void shouldSaveSchedule() {
        MealSchedule savedSchedule = mealScheduleRepository.saveAndFlush(generateTestSchedule(DayOfWeek.FRIDAY, MealType.DINNER));
        Assertions.assertNotNull(savedSchedule.getId());
        Assertions.assertEquals(DayOfWeek.FRIDAY, savedSchedule.getSelectedDay());
        Assertions.assertEquals(MealType.DINNER, savedSchedule.getMealType());
        Assertions.assertNotNull(savedSchedule.getCreatedAt());
        Assertions.assertEquals(testRecipe.getId(), savedSchedule.getRecipe().getId());
        Assertions.assertEquals(testUser.getId(), savedSchedule.getRecipe().getUser().getId());
    }

    @Test
    @Order(2)
    void shouldFindScheduleByUserId() {
        mealScheduleRepository.saveAndFlush(generateTestSchedule(DayOfWeek.FRIDAY, MealType.DINNER));
        mealScheduleRepository.saveAndFlush(generateTestSchedule(DayOfWeek.MONDAY, MealType.LUNCH));
        List<MealSchedule> schedules = mealScheduleRepository.findByUserId(testUser.getId());
        Assertions.assertFalse(schedules.isEmpty());
        Assertions.assertEquals(2, schedules.size());
        Assertions.assertEquals(DayOfWeek.FRIDAY, schedules.get(0).getSelectedDay());
        Assertions.assertEquals(MealType.DINNER, schedules.get(0).getMealType());
        Assertions.assertEquals(DayOfWeek.MONDAY, schedules.get(1).getSelectedDay());
        Assertions.assertEquals(MealType.LUNCH, schedules.get(1).getMealType());
    }

    @Test
    @Order(3)
    void shouldFailOnSameUserAndDayAndMealType() {
        mealScheduleRepository.saveAndFlush(generateTestSchedule(DayOfWeek.FRIDAY, MealType.DINNER));
        Assertions.assertThrows(Exception.class, () -> mealScheduleRepository.saveAndFlush(generateTestSchedule(DayOfWeek.FRIDAY, MealType.DINNER)));
    }

}
