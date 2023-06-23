package kr.rogarithm.todos.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequest {

    private String account;
    private String password;
}