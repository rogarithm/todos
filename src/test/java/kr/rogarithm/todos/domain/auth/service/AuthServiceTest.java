package kr.rogarithm.todos.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.exception.AuthenticationFailedException;
import kr.rogarithm.todos.domain.user.dao.UserMapper;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.global.auth.JwtGenerator;
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

    @Mock
    JwtGenerator jwtGenerator;

    @Test
    public void failLoginWhenAccountIsNotJoinedYet() {

        //given
        LoginRequest request = LoginRequest.builder()
                                           .account("sehoongim")
                                           .password("q1w2e3!")
                                           .build();

        //when
        when(userMapper.selectUserByAccount(request.getAccount())).thenThrow(AuthenticationFailedException.class);

        //then
        assertThrows(AuthenticationFailedException.class, () -> authService.loginUser(request));

        verify(userMapper).selectUserByAccount(request.getAccount());
    }

    @Test
    public void failLoginWhenPasswordIsNotMatched() {

        //given
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

        //when
        when(userMapper.selectUserByAccount(request.getAccount())).thenReturn(user);

        //then
        assertThrows(AuthenticationFailedException.class, () -> authService.loginUser(request));

        verify(userMapper).selectUserByAccount(request.getAccount());
    }

    @Test
    public void tokensShouldBePublishedWhenJoinedUserLogin() {

        //given
        LoginRequest request = LoginRequest.builder()
                                           .account("sehoongim")
                                           .password("q1w2e3!")
                                           .build();

        User user = User.builder()
                        .account("sehoongim")
                        .password("q1w2e3!")
                        .nickname("shrimp-cracker")
                        .phone("010-1010-1010")
                        .crn("123-45-67890")
                        .build();

        Date now = new Date();
        String accessToken = Jwts.builder()
                              .setSubject(request.getAccount())
                              .setIssuedAt(now)
                              .signWith(SignatureAlgorithm.HS256, "secret")
                              .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                              .setIssuer("higher-x.com")
                              .setExpiration(new Date(now.getTime() + Duration.ofDays(1).toMillis()))
                              .compact();
        String refreshToken = Jwts.builder()
                                 .setSubject(request.getAccount())
                                 .setIssuedAt(now)
                                 .signWith(SignatureAlgorithm.HS256, "secret")
                                 .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                                 .setIssuer("higher-x.com")
                                 .setExpiration(new Date(now.getTime() + Duration.ofDays(30).toMillis()))
                                 .compact();

        //when
        when(userMapper.selectUserByAccount(request.getAccount())).thenReturn(user);
        when(jwtGenerator.generateAccessToken(request)).thenReturn(accessToken);
        when(jwtGenerator.generateRefreshToken(request)).thenReturn(refreshToken);

        //then
        assertThat(authService.loginUser(request)).isNotNull();

        verify(userMapper).selectUserByAccount(request.getAccount());
        verify(jwtGenerator).generateAccessToken(request);
        verify(jwtGenerator).generateRefreshToken(request);
    }
}