package kr.rogarithm.todos.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {

    private String accessToken;
}