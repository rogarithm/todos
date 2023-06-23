package kr.rogarithm.todos.domain.auth.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.exception.AuthenticationFailedException;
import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserMapper userMapper;

    @Test
    public void failLoginWhenAccountIsNotJoinedYet() {

        LoginRequest request = LoginRequest.builder()
                                           .account("sehoongim")
                                           .password("q1w2e3!")
                                           .build();

        when(userMapper.selectUserByAccount(request.getAccount())).thenThrow(AuthenticationFailedException.class);

        Assertions.assertThrows(AuthenticationFailedException.class, () -> authService.loginUser(request));

        verify(userMapper).selectUserByAccount(request.getAccount());
    }

    @Test
    public void failLoginWhenPasswordIsNotMatched() {

        LoginRequest request = LoginRequest.builder()
                                           .account("sehoongim")
                                           .password("not-matched")
                                           .build();

        User user = User.builder()
                        .account("sehoongim")
                        .password("q1w2e3!")
                        .nickname("shrimp-cracker")
                        .phone("010-1010-1010")
                        .crn("123-45-67890")
                        .build();

        when(userMapper.selectUserByAccount(request.getAccount())).thenReturn(user);

        Assertions.assertThrows(AuthenticationFailedException.class, () -> authService.loginUser(request));

        verify(userMapper).selectUserByAccount(request.getAccount());
    }
}