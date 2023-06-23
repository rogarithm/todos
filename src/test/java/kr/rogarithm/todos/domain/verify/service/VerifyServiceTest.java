package kr.rogarithm.todos.domain.verify.service;

import static org.mockito.Mockito.when;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.exception.DuplicateAccountException;
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

        when(userMapper.selectUserByAccount(account)).thenThrow(DuplicateAccountException.class);

        Assertions.assertThrows(DuplicateAccountException.class, () -> verifyService.isDuplicated(account));
    }

}