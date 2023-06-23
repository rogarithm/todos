package kr.rogarithm.todos.domain.auth.service;

import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.dto.LoginResponse;
import kr.rogarithm.todos.domain.auth.exception.AuthenticationFailedException;
import kr.rogarithm.todos.domain.user.dao.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public LoginResponse loginUser(LoginRequest request) {

        String account = request.getAccount();

        if (userMapper.selectUserByAccount(account) == null) {
            throw new AuthenticationFailedException("입력한 계정(" + account + ")으로 가입된 회원 정보가 없습니다");
        }

        return null;
    }
}