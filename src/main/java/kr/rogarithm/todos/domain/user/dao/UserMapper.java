package kr.rogarithm.todos.domain.user.dao;

import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insertUser(JoinUserRequest joinUserRequest);

    User selectUserByAccount(String account);

    User selectuserByNickname(String nickname);
}