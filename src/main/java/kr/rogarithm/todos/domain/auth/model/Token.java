package kr.rogarithm.todos.domain.auth.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Token {

    private String accessToken;
}