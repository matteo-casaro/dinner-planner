package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("IntegrationTest")
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User generateTestUser() {
        return User.builder()
                .email("john_doe@email.org")
                .password("password")
                .build();
    }

    @Test
    @Order(1)
    void shouldSaveUser() {
        User savedUser = userRepository.saveAndFlush(generateTestUser());
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("john_doe@email.org", savedUser.getEmail());
        Assertions.assertEquals("password", savedUser.getPassword());
    }

    @Test
    @Order(2)
    void shouldFindUserByEmail() {
        userRepository.saveAndFlush(generateTestUser());
        Optional<User> user = userRepository.findByEmail("john_doe@email.org");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("john_doe@email.org", user.get().getEmail());
        Assertions.assertEquals("password", user.get().getPassword());
    }

    @Test
    @Order(3)
    void shouldCreateTempToken() {
        User toUpdate = userRepository.saveAndFlush(generateTestUser());
        toUpdate.setTempToken("tempToken");
        toUpdate.setTempTokenExp(LocalDateTime.now());
        userRepository.saveAndFlush(toUpdate);
        Optional<User> toCheck = userRepository.findByEmail("john_doe@email.org");
        Assertions.assertTrue(toCheck.isPresent());
        Assertions.assertEquals("tempToken", toCheck.get().getTempToken());
        Assertions.assertNotNull(toCheck.get().getTempTokenExp());
    }

    @Test
    @Order(4)
    void shouldFindUserByTempToken() {
        User toUpdate = userRepository.saveAndFlush(generateTestUser());
        toUpdate.setTempToken("tempToken");
        toUpdate.setTempTokenExp(LocalDateTime.now());
        userRepository.saveAndFlush(toUpdate);
        Optional<User> toCheck = userRepository.findByTempToken("tempToken");
        Assertions.assertTrue(toCheck.isPresent());
        Assertions.assertEquals("tempToken", toCheck.get().getTempToken());
        Assertions.assertNotNull(toCheck.get().getTempTokenExp());
    }

    @Test
    @Order(5)
    void shouldFailOnSameEmail() {
        userRepository.saveAndFlush(generateTestUser());
        Assertions.assertThrows(Exception.class, () -> userRepository.saveAndFlush(generateTestUser()));
    }

    @Test
    @Order(6)
    void shouldNotFindUserByEmail() {
        Optional<User> user = userRepository.findByEmail("pippo");
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    @Order(7)
    void shouldNotFindUserByTempToken() {
        Optional<User> user = userRepository.findByTempToken("pippo");
        Assertions.assertTrue(user.isEmpty());
    }

}
