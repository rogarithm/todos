package kr.rogarithm.todos.domain.verify.service;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.verify.dto.VerifyResponse;
import kr.rogarithm.todos.domain.verify.exception.VerificationException;
import org.springframework.stereotype.Service;

@Service
public class VerifyService {

    private final UserMapper userMapper;

    public VerifyService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public VerifyResponse isDuplicatedAccount(String account) {

        if (userMapper.selectUserByAccount(account) != null) {
            throw new VerificationException(
                    "입력한 계정(" + account + ")으로 등록된 회원이 이미 존재합니다."
            );
        }

        return VerifyResponse.builder()
                             .verify(true)
                             .build();
    }

    public VerifyResponse isDuplicatedNickname(String nickname) {

        if (userMapper.selectUserByNickname(nickname) != null) {
            throw new VerificationException(
                    "입력한 닉네임(" + nickname + ")으로 등록된 회원이 이미 존재합니다."
            );
        }

        return VerifyResponse.builder()
                             .verify(true)
                             .build();
    }
}