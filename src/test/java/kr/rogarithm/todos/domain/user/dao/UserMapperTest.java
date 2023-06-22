package kr.rogarithm.todos.domain.user.dao;

import kr.rogarithm.todos.domain.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper.deleteAllUsers();
    }

    @Test
    public void insertUserSuccess() {
        User user = User.builder()
                        .account("sehoongim")
                        .password("q1w2e3!")
                        .nickname("shrimp-cracker")
                        .phone("010-1010-1010")
                        .crn("123-45-67890")
                        .build();

        userMapper.insertUser(user);
    }

    @Test
    public void findUserByAccountFail() {
        String invalidAccount = "non-existing";
        User user = userMapper.selectUserByAccount(invalidAccount);
        Assertions.assertThat(user).isNull();
    }

    @Test
    public void findUserByAccountSuccess() {
        User user = User.builder()
                        .account("sehoonlee")
                        .password("q1w2e3!")
                        .nickname("shrimp-cracker")
                        .phone("010-1010-1010")
                        .crn("123-45-67890")
                        .build();

        userMapper.insertUser(user);

        String validAccount = "sehoonlee";
        User retrieved = userMapper.selectUserByAccount(validAccount);
        Assertions.assertThat(retrieved).isNotNull();
    }

    @Test
    public void findUserByNicknameFail() {
        String invalidNickname = "non-existing";
        User user = userMapper.selectUserByNickname(invalidNickname);
        Assertions.assertThat(user).isNull();
    }
}