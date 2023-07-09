package kr.rogarithm.todos.domain.todo.dao;

import java.util.List;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodoMapperTest {

    @Autowired
    TodoMapper todoMapper;

    @Test
    public void selectTodoFailsWhenIdIsInvalid() {

        Long invalidId = -1L;
        Todo todo = todoMapper.selectTodoById(invalidId);
        Assertions.assertThat(todo).isNull();
    }

    @Test
    public void selectTodoSuccessWhenIdIsValid() {

        Long validId = 1L;
        Todo todo = todoMapper.selectTodoById(validId);
        Assertions.assertThat(todo.getId()).isEqualTo(validId);
    }

    @Test
    public void insertTodoSuccessWhenTodoItemIsValid() {

        Todo todo = AddTodoRequest.builder()
                                  .name("물 사기")
                                  .description("집 앞 슈퍼에서 물 사오기")
                                  .build()
                                  .toTodo();

        int affected = todoMapper.insertTodo(todo);
        Assertions.assertThat(affected).isEqualTo(1);
        todoMapper.deleteTodoByNameAndDescription(todo.getName(), todo.getDescription());
    }

    @Test
    public void selectAllTodos() {

        Long size = 3L;
        String state = "ALL";
        List<Todo> todos = todoMapper.selectTodos(state, size);
        Assertions.assertThat(todos.size()).isEqualTo(3);
    }

    @Test
    public void selectCompleteTodos() {

        Long size = 2L;
        String state = "COMPLETE";
        List<Todo> todos = todoMapper.selectTodos(state, size);
        Assertions.assertThat(todos.size()).isEqualTo(1);
    }

    @Test
    public void selectIncompleteTodos() {

        Long size = 3L;
        String state = "INCOMPLETE";
        List<Todo> todos = todoMapper.selectTodos(state, size);
        Assertions.assertThat(todos.size()).isEqualTo(2);
    }
}