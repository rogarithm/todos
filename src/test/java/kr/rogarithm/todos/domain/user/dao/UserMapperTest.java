package kr.rogarithm.todos.domain.user.dao;

import kr.rogarithm.todos.domain.user.domain.User;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    EasyRandom generator;

    User user;

    @BeforeEach
    public void setUp() {
        generator = new EasyRandom();
        user = generator.nextObject(User.class);
        userMapper.deleteAllUsers();
    }

    @Test
    public void insertUserSuccess() {

        userMapper.insertUser(user);
    }

    @Test
    public void findUserByAccountFail() {

        String invalidAccount = user.getAccount().replaceAll("[a-zA-Z0-9]", "X");

        User user = userMapper.selectUserByAccount(invalidAccount);
        Assertions.assertThat(user).isNull();
    }

    @Test
    public void findUserByAccountSuccess() {

        userMapper.insertUser(user);
        String validAccount = user.getAccount();

        User retrieved = userMapper.selectUserByAccount(validAccount);
        Assertions.assertThat(retrieved).isNotNull();
    }

    @Test
    public void findUserByNicknameFail() {

        String invalidNickname = user.getNickname().replaceAll("[a-zA-Z0-9]", "X");

        User user = userMapper.selectUserByNickname(invalidNickname);
        Assertions.assertThat(user).isNull();
    }

    @Test
    public void findUserByNicknameSuccess() {

        userMapper.insertUser(user);
        String validNickname = user.getNickname();

        User retrieved = userMapper.selectUserByNickname(validNickname);
        Assertions.assertThat(retrieved).isNotNull();
    }
}