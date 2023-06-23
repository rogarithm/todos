package kr.rogarithm.todos.domain.auth.service;

import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.dto.LoginResponse;
import kr.rogarithm.todos.domain.auth.exception.AuthenticationFailedException;
import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public LoginResponse loginUser(LoginRequest request) {

        String account = request.getAccount();
        String password = request.getPassword();
        User user = userMapper.selectUserByAccount(account);

        if (user == null) {
            throw new AuthenticationFailedException("입력한 계정(" + account + ")으로 가입된 회원 정보가 없습니다");
        }

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationFailedException("잘못된 비밀번호입니다");
        }

        return null;
    }
}