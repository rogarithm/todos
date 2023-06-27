package kr.rogarithm.todos.domain.todo.end2end;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoEnd2EndTest {

    @LocalServerPort
    int port;

    @Test
    public void getTodoItemByIdSuccess() throws Exception {

        RestAssured.port = port;

        Long validId = 1L;

        ExtractableResponse<Response> response = getTodoById(validId);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    public void getTodoItemByIdFail() throws Exception {

        RestAssured.port = port;

        Long validId = -1L;

        ExtractableResponse<Response> response = getTodoById(validId);

        assertThat(response.statusCode()).isEqualTo(404);
    }

    public static ExtractableResponse<Response> getTodoById(Long todoId) {

        return RestAssured
                .given().log().all()
                .pathParams("todoId", todoId)
                .when()
                .get("/todo/{todoId}")
                .then()
                .log().all()
                .extract();
    }
}