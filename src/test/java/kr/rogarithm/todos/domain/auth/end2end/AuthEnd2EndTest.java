package kr.rogarithm.todos.domain.auth.end2end;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthEnd2EndTest {

    @LocalServerPort
    int port;

    @Test
    public void loginUserSuccess() throws Exception {

        RestAssured.port = port;

        LoginRequest request = LoginRequest.builder()
                                           .account("sehoongim")
                                           .password("q1w2e3!")
                                           .build();
        ExtractableResponse<Response> response = loginUser(request);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    public static ExtractableResponse<Response> loginUser(LoginRequest loginRequest) {

        return RestAssured
                .given().log().all()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post("auth/login")
                .then()
                .log().all()
                .extract();
    }
}