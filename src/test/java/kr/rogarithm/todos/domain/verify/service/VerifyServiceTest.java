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

        Assertions.assertThrows(VerificationException.class, () -> verifyService.isDuplicatedAccount(account));
    }

    @Test
    public void successVerificationWhenAccountIsNotDuplicated() {

        String account = "sehoongim";

        when(userMapper.selectUserByAccount(account)).thenReturn(null);

        assertThat(verifyService.isDuplicatedAccount(account).getVerify()).isEqualTo(true);
    }

    @Test
    public void failVerificationWhenNicknameIsDuplicated() {

        String nickname = "shrimp-cracker";

        User user = User.builder()
                        .account("sehoongim")
                        .password("q1w2e3!")
                        .nickname("shrimp-cracker")
                        .phone("010-1010-1010")
                        .crn("123-45-67890")
                        .build();

        when(userMapper.selectUserByNickname(nickname)).thenReturn(user);

        Assertions.assertThrows(VerificationException.class, () -> verifyService.isDuplicatedNickname(nickname));
    }

    @Test
    public void successVerificationWhenNicknameIsNotDuplicated() {

        String nickname = "shrimp-cracker";

        when(userMapper.selectUserByNickname(nickname)).thenReturn(null);

        assertThat(verifyService.isDuplicatedNickname(nickname).getVerify()).isEqualTo(true);
    }

}