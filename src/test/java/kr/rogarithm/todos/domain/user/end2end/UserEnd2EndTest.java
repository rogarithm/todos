package kr.rogarithm.todos.domain.user.end2end;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.rogarithm.todos.domain.user.domain.User;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserEnd2EndTest {

    @LocalServerPort
    int port;

    User validUser;

    User userDuplicateAccount;

    User userDuplicateNickname;

    User userInvalidCrn;

    @BeforeEach
    public void setUp() {

        validUser = User.builder()
                        .account("adam")
                        .password("g1it0r!")
                        .nickname("guitar-guru")
                        .phone("010-1010-1010")
                        .crn("123-45-67890")
                        .build();

        userDuplicateAccount = User.builder()
                                   .account("sehoon")
                                   .password("q1w2e3!")
                                   .nickname("shrimp-cracker")
                                   .phone("010-1010-1010")
                                   .crn("123-45-67890")
                                   .build();

        userDuplicateNickname = User.builder()
                                   .account("kate")
                                   .password("q1w2e3!")
                                   .nickname("shrimp-cracker")
                                   .phone("010-1010-1010")
                                   .crn("123-45-67890")
                                   .build();

        userInvalidCrn = User.builder()
                             .account("bill")
                             .password("q1w2e3!")
                             .nickname("kill-bill")
                             .phone("010-1010-1010")
                             .crn("123-45-11111")
                             .build();
    }

    @Test
    public void joinUserSuccess() throws Exception {

        RestAssured.port = port;

        ExtractableResponse<Response> response = joinUser(JoinUserRequest.of(validUser));

        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    public void joinUserFailWhenDuplicateAccount() throws Exception {

        RestAssured.port = port;

        ExtractableResponse<Response> response = joinUser(JoinUserRequest.of(userDuplicateAccount));

        assertThat(response.statusCode()).isEqualTo(409);
    }

    @Test
    public void joinUserFailWhenDuplicateNickname() throws Exception {

        RestAssured.port = port;

        ExtractableResponse<Response> response = joinUser(JoinUserRequest.of(userDuplicateNickname));

        assertThat(response.statusCode()).isEqualTo(409);
    }

    @Test
    public void joinUserFailWhenInvalidCrn() throws Exception {

        RestAssured.port = port;

        ExtractableResponse<Response> response = joinUser(JoinUserRequest.of(userInvalidCrn));

        assertThat(response.statusCode()).isEqualTo(409);
    }

    public static ExtractableResponse<Response> joinUser(JoinUserRequest joinUserRequest) {

        return RestAssured
                .given().log().all()
                .contentType("application/json")
                .body(joinUserRequest)
                .when()
                .post("/user")
                .then()
                .log().all()
                .extract();
    }
}