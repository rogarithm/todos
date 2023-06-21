package kr.rogarithm.todos.domain.user.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class User {

    private Long id;
    private String account;
    private String password;
    private String nickname;
    private String phone;
    private String crn;
    private LocalDateTime createdAt;
}