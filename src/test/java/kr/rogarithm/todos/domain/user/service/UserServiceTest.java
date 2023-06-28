package kr.rogarithm.todos.domain.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import kr.rogarithm.todos.domain.user.exception.DuplicateAccountException;
import kr.rogarithm.todos.domain.user.exception.DuplicateNicknameException;
import kr.rogarithm.todos.domain.user.exception.InvalidCompanyRegistrationNumberException;
import kr.rogarithm.todos.domain.user.validate.CompanyRegistrationNumberValidator;
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

    @Mock
    CompanyRegistrationNumberValidator validator;

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
        when(userMapper.selectUserByNickname(request.getNickname())).thenReturn(null);
        when(validator.verifyCompanyRegistrationNumber(request.getCrn())).thenReturn(true);
        doNothing().when(userMapper).insertUser(any(User.class));

        //then
        userService.registerUser(request);
        verify(userMapper).selectUserByAccount(request.getAccount());
        verify(userMapper).selectUserByNickname(request.getNickname());
        verify(validator).verifyCompanyRegistrationNumber(request.getCrn());
        verify(userMapper).insertUser(any(User.class));
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
        when(userMapper.selectUserByNickname(requestWithDuplicateNickname.getNickname())).thenReturn(user);

        //then
        Assertions.assertThrows(DuplicateNicknameException.class,
                () -> userService.registerUser(requestWithDuplicateNickname));
        verify(userMapper).selectUserByAccount(requestWithDuplicateNickname.getAccount());
        verify(userMapper).selectUserByNickname(requestWithDuplicateNickname.getNickname());
    }

    @Test
    public void failRegisterUserWhenCrdIsInvalid() {

        //given
        JoinUserRequest requestWithInvalidCrn = JoinUserRequest.of(user);

        //when
        when(userMapper.selectUserByAccount(requestWithInvalidCrn.getAccount())).thenReturn(null);
        when(userMapper.selectUserByNickname(requestWithInvalidCrn.getNickname())).thenReturn(null);
        when(validator.verifyCompanyRegistrationNumber(requestWithInvalidCrn.getCrn())).thenReturn(false);

        //then
        Assertions.assertThrows(InvalidCompanyRegistrationNumberException.class,
                () -> userService.registerUser(requestWithInvalidCrn));
        verify(userMapper).selectUserByAccount(requestWithInvalidCrn.getAccount());
        verify(userMapper).selectUserByNickname(requestWithInvalidCrn.getNickname());
        verify(validator).verifyCompanyRegistrationNumber(requestWithInvalidCrn.getCrn());
    }
}