package kr.rogarithm.todos.domain.verify.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.domain.user.validate.CompanyRegistrationNumberValidator;
import kr.rogarithm.todos.domain.verify.exception.VerificationException;
import org.jeasy.random.EasyRandom;
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

    EasyRandom generator;

    User user;

    @BeforeEach
    public void setUpGiven() {

        generator = new EasyRandom();
        user = generator.nextObject(User.class);
    }

    @Test
    public void failVerificationWhenAccountIsDuplicated() {

        when(userMapper.selectUserByAccount(any(String.class))).thenReturn(user);

        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedAccount(user.getAccount()));
    }

    @Test
    public void successVerificationWhenAccountIsNotDuplicated() {

        when(userMapper.selectUserByAccount(any(String.class))).thenReturn(null);

        assertThat(verifyService.isDuplicatedAccount(user.getAccount()).getVerify()).isEqualTo(true);
    }

    @Test
    public void failVerificationWhenNicknameIsDuplicated() {

        when(userMapper.selectUserByNickname(any(String.class))).thenReturn(user);

        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedNickname(user.getNickname()));
    }

    @Test
    public void successVerificationWhenNicknameIsNotDuplicated() {

        when(userMapper.selectUserByNickname(any(String.class))).thenReturn(null);

        assertThat(verifyService.isDuplicatedNickname(user.getNickname()).getVerify()).isEqualTo(true);
    }

    @Test
    public void failVerificationWhenCrdIsInvalid() {

        when(crnValidator.verifyCompanyRegistrationNumber(any(String.class))).thenReturn(false);

        assertThrows(VerificationException.class, () -> verifyService.isValidCrn(user.getCrn()));
    }

    @Test
    public void successVerificationWhenCrdIsValid() {

        when(crnValidator.verifyCompanyRegistrationNumber(any(String.class))).thenReturn(true);

        assertThat(verifyService.isValidCrn(user.getCrn()).getVerify()).isEqualTo(true);
    }

}