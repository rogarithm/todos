package kr.rogarithm.todos.domain.auth.controller;

import javax.servlet.http.HttpServletResponse;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.dto.LoginResponse;
import kr.rogarithm.todos.domain.auth.model.Token;
import kr.rogarithm.todos.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        Token tokens = authService.loginUser(loginRequest);
        ResponseCookie cookie = ResponseCookie.from("RefreshToken", tokens.getRefreshToken())
                                              .maxAge(7 * 24 * 60 * 60)
                                              .path("/")
                                              .secure(true)
                                              .httpOnly(true)
                                              .build();

        response.setHeader("Set-Cookie", cookie.toString());
        response.setHeader("Authorization", "Bearer " + tokens.getAccessToken());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(LoginResponse.builder()
                                                .accessToken(tokens.getAccessToken())
                                                .build()
                             );
    }
}