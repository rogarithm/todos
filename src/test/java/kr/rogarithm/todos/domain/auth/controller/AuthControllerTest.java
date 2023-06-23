package kr.rogarithm.todos.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.dto.LoginResponse;
import kr.rogarithm.todos.domain.auth.exception.AuthenticationFailedException;
import kr.rogarithm.todos.domain.auth.service.AuthService;
import kr.rogarithm.todos.global.auth.JwtGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    AuthController authController;

    @MockBean
    JwtGenerator jwtGenerator;

    @MockBean
    AuthService authService;

    @Test
    public void refreshTokenShouldBePublishedWhenJoinedUserLogin() throws Exception {

        LoginRequest request = LoginRequest.builder()
                                           .account("sehoongim")
                                           .password("q1w2e3!")
                                           .build();

        Date now = new Date();
        String jwtToken = Jwts.builder()
                              .setSubject(request.getAccount())
                              .setIssuedAt(now)
                              .signWith(SignatureAlgorithm.HS256, "secret")
                              .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                              .setIssuer("higher-x.com")
                              .setExpiration(new Date(now.getTime() + Duration.ofDays(30).toMillis()))
                              .compact();

        LoginResponse response = LoginResponse.builder()
                                              .accessToken(jwtToken)
                                              .build();

        when(authService.loginUser(any(LoginRequest.class))).thenReturn(response);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/login")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());

        verify(authService).loginUser(any(LoginRequest.class));
    }

    @Test
    public void loginFailWhenUserDidNotJoin() throws Exception {

        String accountNotJoined = "sehoongim";
        LoginRequest request = LoginRequest.builder()
                                           .account(accountNotJoined)
                                           .password("q1w2e3!")
                                           .build();

        when(authService.loginUser(any(LoginRequest.class))).thenThrow(AuthenticationFailedException.class);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/login")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isUnauthorized());

        verify(authService).loginUser(any(LoginRequest.class));
    }

    @Test
    public void loginFailWhenPasswordIsNotMatched() throws Exception {

        String incorrectPassword = "not-matched";
        LoginRequest request = LoginRequest.builder()
                                           .account("sehoongim")
                                           .password(incorrectPassword)
                                           .build();

        when(authService.loginUser(any(LoginRequest.class))).thenThrow(AuthenticationFailedException.class);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/login")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isUnauthorized());

        verify(authService).loginUser(any(LoginRequest.class));
    }
}