package kr.rogarithm.todos.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyRegistrationNumberValidatorTest {

    CompanyRegistrationNumberValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new CompanyRegistrationNumberValidator();
    }

    @Test
    public void testCrnValidationSteps() {

        String validationKey = "137137135";
        String validCrn = "123-45-67890";

        String stepOne = validCrn.replaceAll("-", "");
        assertThat(stepOne).isEqualTo("1234567890");
        int stepTwo = 0;
        for (int i = 0; i < 9; i++) {
            stepTwo += Integer.parseInt(String.valueOf(stepOne.charAt(i)))
                    * Integer.parseInt(String.valueOf(validationKey.charAt(i)));
        }
        assertThat(stepTwo).isEqualTo(165);

        int lastIndex = 8;
        int stepThree = Integer.parseInt(String.valueOf(stepOne.charAt(lastIndex)))
                * Integer.parseInt(String.valueOf(validationKey.charAt(lastIndex)));
        stepThree /= 10;
        assertThat(stepThree).isEqualTo(4);

        int stepFour = stepTwo + stepThree;
        assertThat(stepFour).isEqualTo(169);

        int stepFive = stepFour % 10;
        assertThat(stepFive).isEqualTo(9);

        int stepSix = 10 - stepFive;
        assertThat(stepSix).isEqualTo(1);
    }

    @Test
    public void verificationSuccessWhenCrnIsValid() {

        String validCrn = "123-45-67890";
        boolean result = validator.verifyCompanyRegistrationNumber(validCrn);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void verificationFailWhenCrnIsInvalid() {

        String invalidCrn = "123-45-11111";
        boolean result = validator.verifyCompanyRegistrationNumber(invalidCrn);

        Assertions.assertThat(result).isFalse();
    }
}