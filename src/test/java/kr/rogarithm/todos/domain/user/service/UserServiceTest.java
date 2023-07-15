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
import org.jeasy.random.EasyRandom;
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

    EasyRandom generator;

    User user;

    JoinUserRequest request;

    @BeforeEach
    public void setUp() {

        generator = new EasyRandom();
        user = generator.nextObject(User.class);
        request = generator.nextObject(JoinUserRequest.class);
    }

    @Test
    public void userIsRegisteredWhenRequestIsValid() {

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

        //when
        when(userMapper.selectUserByAccount(request.getAccount())).thenReturn(user);

        //then
        Assertions.assertThrows(DuplicateAccountException.class,
                () -> userService.registerUser(request));
        verify(userMapper).selectUserByAccount(request.getAccount());
    }

    @Test
    public void failRegisterUserWhenNicknameIsDuplicate() {

        //when
        when(userMapper.selectUserByAccount(request.getAccount())).thenReturn(null);
        when(userMapper.selectUserByNickname(request.getNickname())).thenReturn(user);

        //then
        Assertions.assertThrows(DuplicateNicknameException.class,
                () -> userService.registerUser(request));
        verify(userMapper).selectUserByAccount(request.getAccount());
        verify(userMapper).selectUserByNickname(request.getNickname());
    }

    @Test
    public void failRegisterUserWhenCrdIsInvalid() {

        //when
        when(userMapper.selectUserByAccount(request.getAccount())).thenReturn(null);
        when(userMapper.selectUserByNickname(request.getNickname())).thenReturn(null);
        when(validator.verifyCompanyRegistrationNumber(request.getCrn())).thenReturn(false);

        //then
        Assertions.assertThrows(InvalidCompanyRegistrationNumberException.class,
                () -> userService.registerUser(request));
        verify(userMapper).selectUserByAccount(request.getAccount());
        verify(userMapper).selectUserByNickname(request.getNickname());
        verify(validator).verifyCompanyRegistrationNumber(request.getCrn());
    }
}