package kr.rogarithm.todos.domain.auth.service;

import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.dto.LoginResponse;
import kr.rogarithm.todos.domain.auth.exception.AuthenticationFailedException;
import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.global.auth.JwtGenerator;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final JwtGenerator jwtGenerator;

    public AuthService(UserMapper userMapper, JwtGenerator jwtGenerator) {
        this.userMapper = userMapper;
        this.jwtGenerator = jwtGenerator;
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

        String accessToken = jwtGenerator.generateAccessToken(request);
        String refreshToken = jwtGenerator.generateRefreshToken(request);

        return LoginResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
    }
}