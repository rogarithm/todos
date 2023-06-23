package kr.rogarithm.todos.domain.verify.service;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.verify.dto.VerifyResponse;
import org.springframework.stereotype.Service;

@Service
public class VerifyService {

    private final UserMapper userMapper;

    public VerifyService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public VerifyResponse isDuplicated(String account) {
        userMapper.selectUserByAccount(account);
        return null;
    }
}