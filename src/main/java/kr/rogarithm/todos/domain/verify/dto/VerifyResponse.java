package kr.rogarithm.todos.domain.verify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class VerifyResponse {

    private Boolean verify;
}