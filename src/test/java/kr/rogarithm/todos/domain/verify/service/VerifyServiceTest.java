package kr.rogarithm.todos.domain.verify.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.domain.verify.exception.VerificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerifyServiceTest {

    @InjectMocks
    VerifyService verifyService;

    @Mock
    UserMapper userMapper;

    @Test
    public void failVerificationWhenAccountIsDuplicated() {

        String account = "sehoongim";

        User user = User.builder()
                   .account("sehoongim")
                   .password("q1w2e3!")
                   .nickname("shrimp-cracker")
                   .phone("010-1010-1010")
                   .crn("123-45-67890")
                   .build();

        when(userMapper.selectUserByAccount(account)).thenReturn(user);

        Assertions.assertThrows(VerificationException.class, () -> verifyService.isDuplicated(account));
    }

    @Test
    public void successVerificationWhenAccountIsNotDuplicated() {

        String account = "sehoongim";

        when(userMapper.selectUserByAccount(account)).thenReturn(null);

        assertThat(verifyService.isDuplicated(account).getVerify()).isEqualTo(true);
    }

}