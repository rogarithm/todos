package kr.rogarithm.todos.domain.todo.end2end;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoEnd2EndTest {

    @LocalServerPort
    int port;

    @Test
    public void addTodoItemFail() {

        RestAssured.port = port;

        AddTodoRequest requestViolatesConstraint = AddTodoRequest.builder()
                                                      .name("")
                                                      .description("물 사러 갔다오기")
                                                      .build();

        ExtractableResponse<Response> response = addTodo(requestViolatesConstraint);

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    public void addTodoItemSuccess() {

        RestAssured.port = port;

        AddTodoRequest request = AddTodoRequest.builder()
                                               .name("심부름")
                                               .description("물 사러 갔다오기")
                                               .build();

        ExtractableResponse<Response> response = addTodo(request);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    private ExtractableResponse<Response> addTodo(AddTodoRequest addTodoRequest) {

        return RestAssured
                .given().log().all()
                .contentType("application/json")
                .body(addTodoRequest)
                .when()
                .post("/todo")
                .then()
                .log().all()
                .extract();
    }

    @Test
    public void getTodosSuccess() throws Exception {

        RestAssured.port = port;

        String state = "ALL";
        Long size = 3L;

        ExtractableResponse<Response> response = getTodos(state, size);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    public void getTodosFail() throws Exception {

        RestAssured.port = port;

        String state = "ALL";
        Long size = -1L;

        ExtractableResponse<Response> response = getTodos(state, size);

        assertThat(response.statusCode()).isEqualTo(400);
    }

    public static ExtractableResponse<Response> getTodos(String state, Long size) {

        return RestAssured
                .given().log().all()
                .param("state", state)
                .param("size", size)
                .when()
                .get("/todo")
                .then()
                .log().all()
                .extract();
    }

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

        Long invalidId = -1L;

        ExtractableResponse<Response> response = getTodoById(invalidId);

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