package kr.rogarithm.todos.domain.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
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
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Date;
import javax.servlet.http.Cookie;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.exception.AuthenticationFailedException;
import kr.rogarithm.todos.domain.auth.model.Token;
import kr.rogarithm.todos.domain.auth.service.AuthService;
import kr.rogarithm.todos.global.auth.JwtGenerator;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    EasyRandom generator;

    LoginRequest request;

    String accessToken;

    String refreshToken;

    Token tokens;

    @BeforeEach
    public void setUp() {
        generator = new EasyRandom();
        request = generator.nextObject(LoginRequest.class);

        Date now = new Date();
        accessToken = Jwts.builder()
                          .setSubject(request.getAccount())
                          .setIssuedAt(now)
                          .signWith(Keys.hmacShaKeyFor("TodosAppSecretLengthIsMoreThan256".getBytes()),
                                  SignatureAlgorithm.HS256)
                          .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                          .setIssuer("todos.com")
                          .setExpiration(new Date(now.getTime() + Duration.ofDays(1).toMillis()))
                          .compact();
        refreshToken = Jwts.builder()
                           .setSubject(request.getAccount())
                           .setIssuedAt(now)
                           .signWith(Keys.hmacShaKeyFor("TodosAppSecretLengthIsMoreThan256".getBytes()),
                                   SignatureAlgorithm.HS256)
                           .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                           .setIssuer("todos.com")
                           .setExpiration(new Date(now.getTime() + Duration.ofDays(30).toMillis()))
                           .compact();
        tokens = Token.builder()
                      .accessToken(accessToken)
                      .refreshToken(refreshToken)
                      .build();
    }

    @Test
    public void accessTokenShouldBePublishedWhenJoinedUserLogin() throws Exception {

        //when
        when(authService.loginUser(any(LoginRequest.class))).thenReturn(tokens);

        String content = objectMapper.writeValueAsString(validRequest);

        //then
        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                                             .content(content)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();

        String accessTokenInRequestBody = servletResponse.getContentAsString()
                                                         .replaceAll("\\{|\\}|\"", "")
                                                         .split(":")[1];
        assertThat(accessTokenInRequestBody).isEqualTo(accessToken);

        verify(authService).loginUser(any(LoginRequest.class));
    }

    @Test
    public void accessTokenShouldBeStoredInAuthHeaderWhenJoinedUserLogin() throws Exception {

        //when
        when(authService.loginUser(any(LoginRequest.class))).thenReturn(tokens);

        String content = objectMapper.writeValueAsString(validRequest);

        //then
        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                                             .content(content)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();

        String authorizationHeader = servletResponse.getHeader("Authorization");
        assertThat(authorizationHeader).isEqualTo("Bearer " + accessToken);

        verify(authService).loginUser(any(LoginRequest.class));
    }

    @Test
    public void refreshTokenShouldBeStoredInCookieWhenJoinedUserLogin() throws Exception {

        //when
        when(authService.loginUser(any(LoginRequest.class))).thenReturn(tokens);

        String content = objectMapper.writeValueAsString(request);

        //then
        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                                             .content(content)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();

        Cookie refreshTokenInCookie = servletResponse.getCookie("RefreshToken");
        assertThat(refreshTokenInCookie.getValue()).isEqualTo(refreshToken);

        verify(authService).loginUser(any(LoginRequest.class));
    }

    @Test
    public void loginFailWhenUserDidNotJoin() throws Exception {

        //when
        when(authService.loginUser(any(LoginRequest.class))).thenThrow(AuthenticationFailedException.class);

        String content = objectMapper.writeValueAsString(request);

        //then
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

        //when
        when(authService.loginUser(any(LoginRequest.class))).thenThrow(AuthenticationFailedException.class);

        String content = objectMapper.writeValueAsString(request);

        //then
        mockMvc.perform(post("/auth/login")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isUnauthorized());

        verify(authService).loginUser(any(LoginRequest.class));
    }
}