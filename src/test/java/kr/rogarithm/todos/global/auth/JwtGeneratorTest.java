package kr.rogarithm.todos.global.auth;

import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtGeneratorTest {

    @Test
    public void makeAccessToken() {

        JwtGenerator jwtGenerator = new JwtGenerator();

        LoginRequest request = LoginRequest.builder()
                                           .account("sehoongim")
                                           .password("q1w2e3!")
                                           .build();

        String accessToken = jwtGenerator.getnerateToken(request);
        Assertions.assertThat(accessToken).isNotNull();
    }
}