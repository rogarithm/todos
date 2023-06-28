package kr.rogarithm.todos.domain.todo.dao;

import kr.rogarithm.todos.domain.todo.domain.Todo;
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
}