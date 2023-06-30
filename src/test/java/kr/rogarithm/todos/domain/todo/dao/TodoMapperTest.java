package kr.rogarithm.todos.domain.todo.dao;

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
    public void selectTodoByInvalidId() {

        Long invalidId = -1L;
        Todo todo = todoMapper.selectTodoById(invalidId);
        Assertions.assertThat(todo).isNull();
    }

    @Test
    public void selectTodoByValidId() {

        Long validId = 1L;
        Todo todo = todoMapper.selectTodoById(validId);
        Assertions.assertThat(todo.getId()).isEqualTo(validId);
    }

    @Test
    public void insertTodo() {

        Todo todo = AddTodoRequest.builder()
                                  .name("물 사기")
                                  .description("집 앞 슈퍼에서 물 사오기")
                                  .build()
                                  .toTodo();

        int affected = todoMapper.insertTodo(todo);
        Assertions.assertThat(affected).isEqualTo(1);
    }
}