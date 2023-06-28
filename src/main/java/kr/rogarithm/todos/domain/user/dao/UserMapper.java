package kr.rogarithm.todos.domain.user.dao;

import kr.rogarithm.todos.domain.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insertUser(User user);

    User selectUserByAccount(String account);

    User selectUserByNickname(String nickname);

    void deleteAllUsers();
}