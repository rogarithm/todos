package kr.rogarithm.todos.domain.todo.end2end;

import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.ID;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_VALID_STATE;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.STATE;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.UpdateTodoRequest;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/schema.sql", "/data.sql"})
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

        ExtractableResponse<Response> loginResponse = loginUser();

        ExtractableResponse<Response> response = addTodo(requestViolatesConstraint, loginResponse.header("Authorization"));

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    public void addTodoItemSuccess() {

        RestAssured.port = port;

        AddTodoRequest request = AddTodoRequest.builder()
                                               .name("심부름")
                                               .description("물 사러 갔다오기")
                                               .build();

        ExtractableResponse<Response> loginResponse = loginUser();

        ExtractableResponse<Response> response = addTodo(request, loginResponse.header("Authorization"));

        assertThat(response.statusCode()).isEqualTo(200);
    }

    private ExtractableResponse<Response> addTodo(AddTodoRequest addTodoRequest, String token) {

        return RestAssured
                .given().header("Authorization", token).log().all()
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

        ExtractableResponse<Response> loginResponse = loginUser();

        ExtractableResponse<Response> response = getTodos(state, size, loginResponse.header("Authorization"));

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    public void getTodosFail() throws Exception {

        RestAssured.port = port;

        String state = "ALL";
        Long size = -1L;

        ExtractableResponse<Response> loginResponse = loginUser();

        ExtractableResponse<Response> response = getTodos(state, size, loginResponse.header("Authorization"));

        assertThat(response.statusCode()).isEqualTo(400);
    }

    public static ExtractableResponse<Response> getTodos(String state, Long size, String token) {

        return RestAssured
                .given().header("Authorization", token).log().all()
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

        ExtractableResponse<Response> loginResponse = loginUser();

        ExtractableResponse<Response> response = getTodoById(validId, loginResponse.header("Authorization"));

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    public void getTodoItemByIdFail() throws Exception {

        RestAssured.port = port;

        Long invalidId = -1L;

        ExtractableResponse<Response> loginResponse = loginUser();

        ExtractableResponse<Response> response = getTodoById(invalidId, loginResponse.header("Authorization"));

        assertThat(response.statusCode()).isEqualTo(404);
    }

    public static ExtractableResponse<Response> getTodoById(Long todoId, String token) {

        return RestAssured
                .given().header("Authorization", token).log().all()
                .pathParams("todoId", todoId)
                .when()
                .get("/todo/{todoId}")
                .then()
                .log().all()
                .extract();
    }

    @DisplayName("할 일 수정 요청 성공")
    @Test
    public void updateTodoSuccess() {

        RestAssured.port = port;

        EasyRandomParameters updateTodoParam = new EasyRandomParameters()
                .randomize(ID, () -> 1L)
                .randomize(STATE, IS_VALID_STATE);
        EasyRandom generator = new EasyRandom(updateTodoParam);

        UpdateTodoRequest request = generator.nextObject(UpdateTodoRequest.class);

        ExtractableResponse<Response> loginResponse = loginUser();

        ExtractableResponse<Response> response = updateTodo(request, loginResponse.header("Authorization"));

        assertThat(response.statusCode()).isEqualTo(200);
    }

    public static ExtractableResponse<Response> updateTodo(UpdateTodoRequest updateTodoRequest, String token) {

        return RestAssured
                .given().header("Authorization", token).log().all()
                .contentType("application/json")
                .body(updateTodoRequest)
                .when()
                .put("/todo")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> loginUser() {

        LoginRequest loginRequest = LoginRequest.builder()
            .account("sehoongim")
            .password("q1w2e3!")
            .build();

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