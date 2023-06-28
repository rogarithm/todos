package kr.rogarithm.todos.domain.user.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String account;
    private String password;
    private String nickname;
    private String phone;
    private String crn;
    private LocalDateTime createdAt;
}