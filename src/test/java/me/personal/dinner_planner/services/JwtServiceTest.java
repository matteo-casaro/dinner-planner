package me.personal.dinner_planner.services;

import org.junit.jupiter.api.*;

import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("UnitTest")
class JwtServiceTest {

    @Test
    @Order(1)
    void shouldCreateKey() {
        JwtService jwtService = new JwtService();
        Assertions.assertDoesNotThrow(jwtService::init);
    }

    @Test
    @Order(2)
    void shouldGenerateToken() {
        JwtService jwtService = new JwtService();
        jwtService.init();
        UUID userId = UUID.randomUUID();
        String token = jwtService.generateToken(userId);
        Assertions.assertNotNull(token);
    }

    @Test
    @Order(3)
    void shouldGenerateAndParseTokens() {
        JwtService jwtService = new JwtService();
        jwtService.init();
        UUID userId = UUID.randomUUID();
        String token = jwtService.generateToken(userId);
        UUID parsedUserId = jwtService.validateToken(token);
        Assertions.assertEquals(userId, parsedUserId);
    }
}
