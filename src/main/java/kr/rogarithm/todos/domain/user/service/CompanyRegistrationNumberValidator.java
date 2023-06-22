package kr.rogarithm.todos.domain.user.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CompanyRegistrationNumberValidator {

    private static final List<Integer> VALIDATION_KEY = List.of(1, 3, 7, 1, 3, 7, 1, 3, 5);
    private static final Integer LAST_INDEX = 8;

    public boolean verifyCompanyRegistrationNumber(String companyRegistrationNumber) {

        List<Integer> companyRegistrationNumbers = Arrays.stream(companyRegistrationNumber.replaceAll("-", "")
                                                                                          .split(""))
                                                         .map(Integer::parseInt)
                                                         .toList();

        int stepTwo = 0;
        for (int i = 0; i <= LAST_INDEX; i++) {
            stepTwo += companyRegistrationNumbers.get(i) * VALIDATION_KEY.get(i);
        }
        int stepThree = companyRegistrationNumbers.get(LAST_INDEX) * VALIDATION_KEY.get(LAST_INDEX);
        stepThree /= 10;
        int stepFour = stepTwo + stepThree;
        int stepFive = stepFour % 10;
        int stepSix = 10 - stepFive;

        return stepSix == 1;
    }
}