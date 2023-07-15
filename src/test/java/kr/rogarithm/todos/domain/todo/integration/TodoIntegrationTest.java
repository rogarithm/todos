package kr.rogarithm.todos.domain.todo.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import javax.validation.ConstraintViolationException;
import kr.rogarithm.todos.domain.todo.controller.TodoController;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TodoIntegrationTest {

    @Autowired
    TodoController todoController;

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

        Long size = 1L;
        List<String> validState = List.of("ALL", "INCOMPLETE", "COMPLETE");

        for (String state : validState) {
            todoController.getTodos(state, size);
        }
    }
}