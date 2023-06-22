package kr.rogarithm.todos.domain.user.service;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import kr.rogarithm.todos.domain.user.exception.DuplicateAccountException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void registerUser(JoinUserRequest joinUserRequest) {
        if (userMapper.selectUserByAccount(joinUserRequest.getAccount()) != null) {
            throw new DuplicateAccountException(
                    "입력한 계정(" + joinUserRequest.getAccount() + ")으로 등록된 회원이 이미 존재합니다. 다른 계정으로 다시 시도해주세요"
            );
        }

        if (userMapper.selectuserByNickname(joinUserRequest.getNickname()) == null) {
        }

        userMapper.insertUser(joinUserRequest);
    }
}