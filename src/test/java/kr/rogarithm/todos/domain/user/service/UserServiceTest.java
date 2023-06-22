package kr.rogarithm.todos.domain.user.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
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

    @Test
    public void userIsRegisteredWhenRequestIsValid() {

        //given
        User user = User.builder()
                        .account("sehoongim")
                        .password("q1w2e3!")
                        .nickname("shrimp-cracker")
                        .phone("010-1010-1010")
                        .crn("123-45-67890")
                        .build();

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

}