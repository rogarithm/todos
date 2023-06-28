package kr.rogarithm.todos.domain.user.service;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import kr.rogarithm.todos.domain.user.exception.DuplicateAccountException;
import kr.rogarithm.todos.domain.user.exception.DuplicateNicknameException;
import kr.rogarithm.todos.domain.user.exception.InvalidCompanyRegistrationNumberException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final CompanyRegistrationNumberValidator crnValidator;

    public UserService(UserMapper userMapper, CompanyRegistrationNumberValidator crnValidator) {
        this.userMapper = userMapper;
        this.crnValidator = crnValidator;
    }

    public void registerUser(JoinUserRequest joinUserRequest) {

        if (userMapper.selectUserByAccount(joinUserRequest.getAccount()) != null) {
            throw new DuplicateAccountException(
                    "입력한 계정(" + joinUserRequest.getAccount() + ")으로 등록된 회원이 이미 존재합니다. 다른 계정으로 다시 시도해주세요"
            );
        }

        if (userMapper.selectUserByNickname(joinUserRequest.getNickname()) != null) {
            throw new DuplicateNicknameException(
                    "입력한 닉네임(" + joinUserRequest.getNickname() + ")으로 등록된 회원이 이미 존재합니다. 다른 닉네임으로 다시 시도해주세요"
            );
        }

        if (crnValidator.verifyCompanyRegistrationNumber(joinUserRequest.getCrn()) == false) {
            throw new InvalidCompanyRegistrationNumberException(
                    "입력한 사업자 등록 번호(" + joinUserRequest.getCrn() + ")가 잘못되었습니다. 사업자 번호 식별 규칙을 준수하는 값으로 다시 시도해주세요"
            );
        }

        userMapper.insertUser(joinUserRequest.toUser());
    }
}