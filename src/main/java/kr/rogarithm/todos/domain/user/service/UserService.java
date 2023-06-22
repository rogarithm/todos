package kr.rogarithm.todos.domain.user.service;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void registerUser(JoinUserRequest joinUserRequest) {
        if (userMapper.selectUserByAccount(joinUserRequest.getAccount()) == null) {
        }

        if (userMapper.selectuserByNickname(joinUserRequest.getNickname()) == null) {
        }

        userMapper.insertUser(joinUserRequest);
    }
}