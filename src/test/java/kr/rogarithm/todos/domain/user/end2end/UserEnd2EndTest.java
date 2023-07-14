package kr.rogarithm.todos.domain.user.end2end;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.function.Predicate;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserEnd2EndTest {

    @LocalServerPort
    int port;

    EasyRandom generator;

    JoinUserRequest request;

    JoinUserRequest invalidRequest;

    @BeforeEach
    public void setUp() {

        Predicate<Field> crn = FieldPredicates.named("crn")
                                              .and(FieldPredicates.ofType(String.class))
                                              .and(FieldPredicates.inClass(JoinUserRequest.class));

        EasyRandomParameters joinUserParameters = new EasyRandomParameters()
                .seed(new Random().nextInt())
                .randomize(crn, () -> "123-45-67890");

        generator = new EasyRandom(joinUserParameters);

        request = generator.nextObject(JoinUserRequest.class);

        invalidRequest = JoinUserRequest.builder()
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

        ExtractableResponse<Response> response = joinUser(request);

        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    public void joinUserFailWhenDuplicateAccount() throws Exception {

        RestAssured.port = port;

        joinUser(request);
        ExtractableResponse<Response> response = joinUser(request);

        assertThat(response.statusCode()).isEqualTo(409);
    }

    @Test
    public void joinUserFailWhenDuplicateNickname() throws Exception {

        RestAssured.port = port;

        joinUser(request);
        ExtractableResponse<Response> response = joinUser(request);

        assertThat(response.statusCode()).isEqualTo(409);
    }

    @Test
    public void joinUserFailWhenInvalidCrn() throws Exception {

        RestAssured.port = port;

        ExtractableResponse<Response> response = joinUser(invalidRequest);

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