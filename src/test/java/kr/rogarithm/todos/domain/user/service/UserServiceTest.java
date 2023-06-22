package kr.rogarithm.todos.domain.user.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import kr.rogarithm.todos.domain.user.exception.DuplicateAccountException;
import kr.rogarithm.todos.domain.user.exception.DuplicateNicknameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;

    User user;

    @BeforeEach
    public void setUp() {

        user = User.builder()
                   .account("sehoongim")
                   .password("q1w2e3!")
                   .nickname("shrimp-cracker")
                   .phone("010-1010-1010")
                   .crn("123-45-67890")
                   .build();
    }

    @Test
    public void userIsRegisteredWhenRequestIsValid() {

        //given
        JoinUserRequest request = JoinUserRequest.of(user);

        //when
        when(userMapper.selectUserByAccount(request.getAccount())).thenReturn(null);
        when(userMapper.selectuserByNickname(request.getNickname())).thenReturn(null);
        doNothing().when(userMapper).insertUser(request);

        //then
        userService.registerUser(request);
        verify(userMapper).selectUserByAccount(request.getAccount());
        verify(userMapper).selectuserByNickname(request.getNickname());
        verify(userMapper).insertUser(request);
    }

    @Test
    public void failRegisterUserWhenAccountIsDuplicate() {

        //given
        JoinUserRequest requestWithDuplicateAccount = JoinUserRequest.of(user);

        //when
        when(userMapper.selectUserByAccount(requestWithDuplicateAccount.getAccount())).thenReturn(user);

        //then
        Assertions.assertThrows(DuplicateAccountException.class,
                () -> userService.registerUser(requestWithDuplicateAccount));
        verify(userMapper).selectUserByAccount(requestWithDuplicateAccount.getAccount());
    }

    @Test
    public void failRegisterUserWhenNicknameIsDuplicate() {

        //given
        JoinUserRequest requestWithDuplicateNickname = JoinUserRequest.of(user);

        //when
        when(userMapper.selectUserByAccount(requestWithDuplicateNickname.getAccount())).thenReturn(null);
        when(userMapper.selectuserByNickname(requestWithDuplicateNickname.getNickname())).thenReturn(user);

        //then
        Assertions.assertThrows(DuplicateNicknameException.class,
                () -> userService.registerUser(requestWithDuplicateNickname));
        verify(userMapper).selectUserByAccount(requestWithDuplicateNickname.getAccount());
        verify(userMapper).selectuserByNickname(requestWithDuplicateNickname.getNickname());
    }
}