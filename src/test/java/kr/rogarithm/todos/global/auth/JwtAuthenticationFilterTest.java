package kr.rogarithm.todos.global.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Date;
import javax.servlet.http.Cookie;
import kr.rogarithm.todos.domain.auth.controller.AuthController;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.model.Token;
import kr.rogarithm.todos.domain.auth.service.AuthService;
import kr.rogarithm.todos.domain.todo.controller.TodoController;
import kr.rogarithm.todos.domain.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = {TodoController.class, AuthController.class})
class JwtAuthenticationFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    TodoController todoController;

    @MockBean
    TodoService todoService;

    @InjectMocks
    AuthController authController;

    @MockBean
    AuthService authService;

    @MockBean
    JwtAuthenticationManager jwtAuthenticationManager;

    LoginRequest validRequest;

    String accessToken;

    String refreshToken;

    Token tokens;

    @BeforeEach
    public void setUpGiven() {
        validRequest = LoginRequest.builder()
                                   .account("sehoongim")
                                   .password("q1w2e3!")
                                   .build();
        Date now = new Date();
        accessToken = Jwts.builder()
                          .setSubject(validRequest.getAccount())
                          .setIssuedAt(now)
                          .signWith(Keys.hmacShaKeyFor("TodosAppSecretLengthIsMoreThan256".getBytes()),
                                  SignatureAlgorithm.HS256)
                          .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                          .setIssuer("higher-x.com")
                          .setExpiration(new Date(now.getTime() + Duration.ofDays(1).toMillis()))
                          .compact();
        refreshToken = Jwts.builder()
                           .setSubject(validRequest.getAccount())
                           .setIssuedAt(now)
                           .signWith(Keys.hmacShaKeyFor("TodosAppSecretLengthIsMoreThan256".getBytes()),
                                   SignatureAlgorithm.HS256)
                           .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                           .setIssuer("higher-x.com")
                           .setExpiration(new Date(now.getTime() + Duration.ofDays(30).toMillis()))
                           .compact();
        tokens = Token.builder()
                      .accessToken(accessToken)
                      .refreshToken(refreshToken)
                      .build();
    }

    @Test
    public void failWithUnauthorizedCodeWhenNoAccessToken() throws Exception {

        Long todoId = 1L;

        MockHttpServletRequestBuilder requestNoAuthHeader = MockMvcRequestBuilders.get("/todo/{todoId}", todoId)
                                                                                  .cookie(new Cookie("RefreshToken",
                                                                                          "refresh-token"));

        this.mockMvc.perform(requestNoAuthHeader)
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
    }

    @Test
    public void failWithUnauthorizedCodeWhenRefreshTokenIsExpired() throws Exception {

        Long todoId = 1L;

        when(jwtAuthenticationManager.verifyToken("access-token")).thenThrow(ExpiredJwtException.class);

        MockHttpServletRequestBuilder requestNoAuthHeader = MockMvcRequestBuilders.get("/todo/{todoId}", todoId)
                                                                                  .header("Authorization",
                                                                                          "Bearer " + "access-token")
                                                                                  .cookie(new Cookie("RefreshToken",
                                                                                          "refresh-token"));

        this.mockMvc.perform(requestNoAuthHeader)
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
    }

    @Test
    public void failWithUnauthorizedCodeWhenTokenIsWrongFormat() throws Exception {

        Long todoId = 1L;

        when(jwtAuthenticationManager.verifyToken("access-token-in-wrong-format")).thenThrow(JwtException.class);

        MockHttpServletRequestBuilder requestNoAuthHeader = MockMvcRequestBuilders.get("/todo/{todoId}", todoId)
                                                                                  .header("Authorization", "Bearer "
                                                                                          + "access-token-in-wrong-format")
                                                                                  .cookie(new Cookie("RefreshToken",
                                                                                          "refresh-token"));

        this.mockMvc.perform(requestNoAuthHeader)
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
    }


    @Test
    public void bypassFilterWhenGivenApiNeedNoAuthorization() throws Exception {

        LoginRequest validRequest = LoginRequest.builder()
                                                .account("sehoongim")
                                                .password("q1w2e3!")
                                                .build();

        String content = objectMapper.writeValueAsString(validRequest);

        when(authService.loginUser(any(LoginRequest.class))).thenReturn(tokens);

        this.mockMvc.perform(post("/auth/login")
                                             .content(content)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk());
    }
}