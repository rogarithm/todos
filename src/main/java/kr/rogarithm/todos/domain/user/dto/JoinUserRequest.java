package kr.rogarithm.todos.domain.user.dto;

import kr.rogarithm.todos.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinUserRequest {

    private String account;
    private String password;
    private String nickname;
    private String phone;
    private String crn;

    public static JoinUserRequest of(User user) {

        return JoinUserRequest.builder()
                              .account(user.getAccount())
                              .password(user.getPassword())
                              .nickname(user.getNickname())
                              .phone(user.getPhone())
                              .crn(user.getCrn())
                              .build();
    }

    public User toUser() {

        return User.builder()
                   .account(this.getAccount())
                   .password(this.getPassword())
                   .nickname(this.getNickname())
                   .phone(this.getPhone())
                   .crn(this.getCrn())
                   .build();
    }
}