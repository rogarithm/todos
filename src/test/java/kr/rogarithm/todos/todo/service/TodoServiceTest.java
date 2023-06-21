package kr.rogarithm.todos.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import kr.rogarithm.todos.todo.dao.TodoDao;
import kr.rogarithm.todos.todo.domain.Todo;
import kr.rogarithm.todos.todo.dto.TodoResponse;
import kr.rogarithm.todos.todo.exception.TodoItemNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @InjectMocks
    TodoService todoService;

    @Mock
    TodoDao todoDao;

    @Test
    public void validIdShouldReturnTodoItem() {

        //given
        Long validId = 1L;
        Todo todoItem = Todo.builder()
                            .id(validId)
                            .name("물 사기")
                            .description("집 앞 슈퍼에서 물 사오기")
                            .state("INCOMPLETE")
                            .createdAt(LocalDateTime.of(2023, 6, 21, 10, 30))
                            .build();

        //when
        when(todoDao.selectTodoById(validId)).thenReturn(todoItem);
        TodoResponse todoResponse = todoService.getTodoById(validId);

        //then
        verify(todoDao).selectTodoById(validId);
        assertThat(todoResponse.getId()).isEqualTo(validId);
        assertThat(todoResponse).isNotNull();
    }

    @Test
    public void invalidIdShouldThrowException() {

        //given
        Long invalidId = -1L;

        //when
        when(todoDao.selectTodoById(invalidId)).thenReturn(null);

        //then
        org.junit.jupiter.api.Assertions.assertThrows(TodoItemNotFoundException.class,
                () -> todoService.getTodoById(invalidId));
        verify(todoDao).selectTodoById(invalidId);
    }
}