package kr.rogarithm.todos.domain.auth.controller;

import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.auth.dto.LoginResponse;
import kr.rogarithm.todos.domain.auth.service.AuthService;
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
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok()
                             .body(authService.loginUser(loginRequest));
    }
}