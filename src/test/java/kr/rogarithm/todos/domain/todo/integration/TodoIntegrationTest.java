package kr.rogarithm.todos.domain.todo.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.ConstraintViolationException;
import kr.rogarithm.todos.domain.todo.controller.TodoController;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TodoIntegrationTest {

    @Autowired
    TodoController todoController;

    @Test
    public void addTodoFailWhenRequestNotSatisfyConstraint() {

        AddTodoRequest addTodoRequest = AddTodoRequest.builder()
                                                      .name("")
                                                      .description("집 앞 슈퍼에서 물 사오기")
                                                      .build();

        assertThrows(ConstraintViolationException.class, () -> todoController.addTodo(addTodoRequest));
    }

}