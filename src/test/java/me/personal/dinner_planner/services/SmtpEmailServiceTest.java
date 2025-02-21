package me.personal.dinner_planner.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
@Tag("IntegrationTest")
class SmtpEmailServiceTest {

    private final SmtpEmailService smtpEmailService;

    @Autowired
    public SmtpEmailServiceTest(SmtpEmailService smtpEmailService) {
        this.smtpEmailService = smtpEmailService;
    }

    @Test
    void shouldSendPasswordRecoveryEmail() {
        Assertions.assertDoesNotThrow(() ->
                smtpEmailService.sendPasswordResetEmail("test@test.com", "1234")
        );
    }

}
