package kr.rogarithm.todos.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import kr.rogarithm.todos.domain.user.validate.CompanyRegistrationNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyRegistrationNumberValidatorTest {

    CompanyRegistrationNumberValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new CompanyRegistrationNumberValidator();
    }

    @Test
    public void verificationSuccessWhenCrnIsValid() {

        String validCrn = "123-45-67890";

        boolean result = validator.verifyCompanyRegistrationNumber(validCrn);

        assertThat(result).isTrue();
    }

    @Test
    public void verificationFailWhenCrnIsInvalid() {

        String invalidCrn = "123-45-11111";

        boolean result = validator.verifyCompanyRegistrationNumber(invalidCrn);

        assertThat(result).isFalse();
    }
}