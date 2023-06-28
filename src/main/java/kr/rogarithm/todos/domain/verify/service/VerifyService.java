package kr.rogarithm.todos.domain.verify.service;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.validate.CompanyRegistrationNumberValidator;
import kr.rogarithm.todos.domain.verify.dto.VerifyResponse;
import kr.rogarithm.todos.domain.verify.exception.VerificationException;
import org.springframework.stereotype.Service;

@Service
public class VerifyService {

    private final CompanyRegistrationNumberValidator validator;
    private final UserMapper userMapper;

    public VerifyService(CompanyRegistrationNumberValidator validator, UserMapper userMapper) {
        this.validator = validator;
        this.userMapper = userMapper;
    }

    public VerifyResponse isDuplicatedAccount(String account) {

        if (userMapper.selectUserByAccount(account) != null) {
            throw new VerificationException(
                    "입력한 계정(" + account + ")으로 등록된 회원이 이미 존재합니다."
            );
        }

        return new VerifyResponse(true);
    }

    public VerifyResponse isDuplicatedNickname(String nickname) {

        if (userMapper.selectUserByNickname(nickname) != null) {
            throw new VerificationException(
                    "입력한 닉네임(" + nickname + ")으로 등록된 회원이 이미 존재합니다."
            );
        }

        return new VerifyResponse(true);
    }

    public VerifyResponse isValidCrn(String crn) {

        if (validator.verifyCompanyRegistrationNumber(crn) == false) {
            throw new VerificationException(
                    "입력하신 " + crn + "은 유효한 사업자등록번호가 아닙니다. 식별 규칙을 만족하는 값을 입력해주세요."
            );
        }

        return new VerifyResponse(true);
    }
}