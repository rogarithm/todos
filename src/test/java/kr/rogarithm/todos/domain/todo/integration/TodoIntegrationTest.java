package kr.rogarithm.todos.domain.todo.integration;

import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.ID;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_INVALID_ID;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_INVALID_NAME;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_INVALID_STATE;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_VALID_ID;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.IS_VALID_STATE;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.NAME;
import static kr.rogarithm.todos.domain.todo.fixture.UpdateTodoParam.STATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import javax.validation.ConstraintViolationException;
import kr.rogarithm.todos.domain.todo.controller.TodoController;
import kr.rogarithm.todos.domain.todo.dao.TodoMapper;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/schema.sql", "/data.sql"})
@SpringBootTest
public class TodoIntegrationTest {

    @Autowired
    TodoController todoController;

    @Autowired
    TodoMapper todoMapper;

    @Autowired
    TodoService todoService;

    Long todoCount = 0L;

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
            todoCount++;
        }
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
    public void getTodosSuccessWhenParameterSatisfyConstraint() {

        Long size = todoCount;
        String state = "ALL";

        assertEquals(todoCount, todoController.getTodos("ALL", todoCount).getBody().size());

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