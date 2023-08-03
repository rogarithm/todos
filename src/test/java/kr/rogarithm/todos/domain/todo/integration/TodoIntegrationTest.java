package kr.rogarithm.todos.domain.todo.integration;

import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.ID;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_INVALID_ID;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_INVALID_NAME;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_INVALID_STATE;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_VALID_ID;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_VALID_STATE;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.NAME;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.STATE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import javax.validation.ConstraintViolationException;
import kr.rogarithm.todos.domain.auth.controller.AuthController;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import kr.rogarithm.todos.domain.todo.controller.TodoController;
import kr.rogarithm.todos.domain.todo.dao.TodoMapper;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.dto.UpdateTodoRequest;
import kr.rogarithm.todos.domain.todo.service.TodoService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@Sql({"/schema.sql", "/data.sql"})
@AutoConfigureMockMvc
@SpringBootTest
public class TodoIntegrationTest {

    @Autowired
    TodoController todoController;

    @Autowired
    AuthController authController;

    @Autowired
    TodoMapper todoMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {

        Predicate<Field> todoId = FieldPredicates.named("id")
                .and(FieldPredicates.ofType(Long.class))
                .and(FieldPredicates.inClass(Todo.class));
        Predicate<Field> todoState = FieldPredicates.named("state")
                .and(FieldPredicates.ofType(String.class))
                .and(FieldPredicates.inClass(Todo.class));
        EasyRandomParameters todoParameters = new EasyRandomParameters()
                .randomize(todoId, new LongRangeRandomizer(1L, 100L))
                .randomize(todoState, () -> List.of("INCOMPLETE", "COMPLETE").get(new Random().nextInt(1)));

        EasyRandom generator = new EasyRandom(todoParameters);

        for (int i=0; i<10; i++) {
            Todo todo = generator.nextObject(Todo.class);
            todoMapper.insertTodo(todo);
        }

        authController.loginUser(loginRequest.builder()
                                             .account("sehoongim")
                                             .password("q1w2e3!")
                .build(), new MockHttpServletResponse());
    }

    @Test
    public void addTodoFailWhenRequestNotSatisfyConstraint() {

        Predicate<Field> name = FieldPredicates.named("name")
                                               .and(FieldPredicates.ofType(String.class))
                                               .and(FieldPredicates.inClass(AddTodoRequest.class));
        EasyRandomParameters invalidName = new EasyRandomParameters().randomize(name, () -> "");

        EasyRandom generator = new EasyRandom(invalidName);
        AddTodoRequest request = generator.nextObject(AddTodoRequest.class);

        assertThrows(ConstraintViolationException.class, () -> todoController.addTodo(request));
    }

    @Test
    public void getTodosFailWhenSizeNotSatisfyConstraint() {

        List<Long> invalidSizes = List.of(-1L, 0L);

        for (Long size : invalidSizes) {
            assertThrows(ConstraintViolationException.class, () -> todoController.getTodos("ALL", size));
        }
    }

    @Test
    public void getTodosFailWhenStateNotSatisfyConstraint() {

        Long size = 1L;
        List<String> invalidState = List.of("INVALID", "NONEXISTING", "STATE");

        for (String state : invalidState) {
            assertThrows(ConstraintViolationException.class, () -> todoController.getTodos(state, size));
        }
    }

    @Test
    public void getTodosSuccessWhenParameterSatisfyConstraint() throws Exception {

        Long size = 2L;
        String state = "ALL";

        TodoResponse todo1 = TodoResponse.builder()
                                         .id(1L)
                                         .name("커피 원두 구입")
                                         .description("디벨로핑룸 가서 커피 원두 사기")
                                         .state("COMPLETE")
                                         .build();
        TodoResponse todo2 = TodoResponse.builder()
                                         .id(2L)
                                         .name("회덮밥 사오기")
                                         .description("바다회 사랑 가서 회덮밥 포장해오기")
                                         .state("INCOMPLETE")
                                         .build();
        when(todoService.getTodos(state, size)).thenReturn(List.of(todo1, todo2));
        mockMvc.perform(get("/todo")
                       .queryParam("state", state)
                       .queryParam("size", size.toString()))
               .andDo(print())
               .andExpect(status().isOk());

        verify(todoService).getTodos(state, size);
    }

    @DisplayName("id 필드가 유효하지 않은 할 일 수정 요청 시 실패")
    @Test
    public void updateTodoFailWhenInvalidIdParameter() {

        EasyRandomParameters todoParameters = new EasyRandomParameters()
                .randomize(ID, IS_INVALID_ID)
                .randomize(STATE, IS_VALID_STATE);

        EasyRandom generator = new EasyRandom(todoParameters);
        assertThrows(ConstraintViolationException.class,
                () -> todoController.updateTodo(generator.nextObject(UpdateTodoRequest.class)));
    }

    @DisplayName("name 필드가 유효하지 않은 할 일 수정 요청 시 실패")
    @Test
    public void updateTodoFailWhenInvalidParameter() {

        EasyRandomParameters todoParameters = new EasyRandomParameters()
                .randomize(ID, IS_VALID_ID)
                .randomize(NAME, IS_INVALID_NAME)
                .randomize(STATE, IS_VALID_STATE);

        EasyRandom generator = new EasyRandom(todoParameters);
        assertThrows(ConstraintViolationException.class,
                () -> todoController.updateTodo(generator.nextObject(UpdateTodoRequest.class)));
    }

    @DisplayName("state 필드가 유효하지 않은 할 일 수정 요청 시 실패")
    @Test
    public void updateTodoFailWhenInvalidStateParameter() {

        EasyRandomParameters todoParameters = new EasyRandomParameters()
                .randomize(ID, IS_VALID_ID)
                .randomize(STATE, IS_INVALID_STATE);

        EasyRandom generator = new EasyRandom(todoParameters);
        assertThrows(ConstraintViolationException.class,
                () -> todoController.updateTodo(generator.nextObject(UpdateTodoRequest.class)));
    }
}