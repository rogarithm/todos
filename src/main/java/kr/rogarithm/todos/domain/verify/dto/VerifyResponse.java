package kr.rogarithm.todos.domain.verify.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VerifyResponse {

    private Boolean verify;
}