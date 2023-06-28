package kr.rogarithm.todos.domain.verify.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.domain.user.validate.CompanyRegistrationNumberValidator;
import kr.rogarithm.todos.domain.verify.exception.VerificationException;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    CompanyRegistrationNumberValidator crnValidator;

    String account;

    String crn;

    String nickname;

    User user;

    @BeforeEach
    public void setUpGiven() {

        crn = "123-45-67890";
        account = "sehoongim";
        nickname = "shrimp-cracker";
        user = User.builder()
                        .account("sehoongim")
                        .password("q1w2e3!")
                        .nickname("shrimp-cracker")
                        .phone("010-1010-1010")
                        .crn("123-45-67890")
                        .build();
    }

    @Test
    public void failVerificationWhenAccountIsDuplicated() {

        when(userMapper.selectUserByAccount(account)).thenReturn(user);

        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedAccount(account));
    }

    @Test
    public void successVerificationWhenAccountIsNotDuplicated() {

        when(userMapper.selectUserByAccount(account)).thenReturn(null);

        assertThat(verifyService.isDuplicatedAccount(account).getVerify()).isEqualTo(true);
    }

    @Test
    public void failVerificationWhenNicknameIsDuplicated() {

        when(userMapper.selectUserByNickname(nickname)).thenReturn(user);

        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedNickname(nickname));
    }

    @Test
    public void successVerificationWhenNicknameIsNotDuplicated() {

        when(userMapper.selectUserByNickname(nickname)).thenReturn(null);

        assertThat(verifyService.isDuplicatedNickname(nickname).getVerify()).isEqualTo(true);
    }

    @Test
    public void failVerificationWhenCrdIsInvalid() {

        when(crnValidator.verifyCompanyRegistrationNumber(crn)).thenReturn(false);

        assertThrows(VerificationException.class, () -> verifyService.isValidCrn(crn));
    }

    @Test
    public void successVerificationWhenCrdIsValid() {

        when(crnValidator.verifyCompanyRegistrationNumber(crn)).thenReturn(true);

        assertThat(verifyService.isValidCrn(crn).getVerify()).isEqualTo(true);
    }

}