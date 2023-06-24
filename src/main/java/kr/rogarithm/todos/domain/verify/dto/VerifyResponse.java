package kr.rogarithm.todos.domain.verify.dto;

import lombok.Getter;

@Getter
public class VerifyResponse {

    private Boolean verify;

    public VerifyResponse(Boolean verify) {
        this.verify = verify;
    }
}