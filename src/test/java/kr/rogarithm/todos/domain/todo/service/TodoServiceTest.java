package kr.rogarithm.todos.domain.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import kr.rogarithm.todos.domain.todo.dao.TodoMapper;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
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
    TodoMapper todoMapper;

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
        when(todoMapper.selectTodoById(validId)).thenReturn(todoItem);
        TodoResponse todoResponse = todoService.getTodoById(validId);

        //then
        verify(todoMapper).selectTodoById(validId);
        assertThat(todoResponse.getId()).isEqualTo(validId);
        assertThat(todoResponse).isNotNull();
    }

    @Test
    public void invalidIdShouldThrowException() {

        //given
        Long invalidId = -1L;

        //when
        when(todoMapper.selectTodoById(invalidId)).thenReturn(null);

        //then
        assertThrows(TodoItemNotFoundException.class,
                () -> todoService.getTodoById(invalidId));
        verify(todoMapper).selectTodoById(invalidId);
    }

    @Test
    public void saveTodoSuccessWhenRequestIsValid() {

        //given
        AddTodoRequest addTodoRequest = AddTodoRequest.builder()
                                                      .name("물 사기")
                                                      .description("집 앞 슈퍼에서 물 사오기")
                                                      .build();
        //when
        when(todoMapper.insertTodo(any(Todo.class))).thenReturn(1);

        //then
        todoService.saveTodo(addTodoRequest);
        verify(todoMapper).insertTodo(any(Todo.class));
    }

    @Test
    public void getTodosSuccess() {

        //given
        Todo todo1 = Todo.builder()
                         .id(1L)
                         .name("커피 원두 구입")
                         .description("디벨로핑룸 가서 커피 원두 사기")
                         .state("COMPLETE")
                         .createdAt(LocalDateTime.of(2023, 7, 6, 0, 0))
                         .build();
        Todo todo2 = Todo.builder()
                         .id(2L)
                         .name("회덮밥 사오기")
                         .description("바다회 사랑 가서 회덮밥 포장해오기")
                         .state("INCOMPLETE")
                         .createdAt(LocalDateTime.of(2023, 7, 6, 0, 0))
                         .build();
        Long size = 2L;
        String state = "ALL";

        //when
        when(todoMapper.selectTodos(state, size)).thenReturn(List.of(todo1, todo2));

        //then
        todoService.getTodos(state, size);
        verify(todoMapper).selectTodos(state, size);
    }

    @Test
    public void getIncompleteTodos() {

        //given
        Todo todo = Todo.builder()
                         .id(2L)
                         .name("회덮밥 사오기")
                         .description("바다회 사랑 가서 회덮밥 포장해오기")
                         .state("INCOMPLETE")
                         .createdAt(LocalDateTime.of(2023, 7, 6, 0, 0))
                         .build();
        Long size = 1L;
        String state = "INCOMPLETE";

        //when
        when(todoMapper.selectTodos(state, size)).thenReturn(List.of(todo));

        //then
        todoService.getTodos(state, size);
        verify(todoMapper).selectTodos(state, size);
    }

    @Test
    public void getCompleteTodos() {

        //given
        Todo todo = Todo.builder()
                         .id(1L)
                         .name("커피 원두 구입")
                         .description("디벨로핑룸 가서 커피 원두 사기")
                         .state("COMPLETE")
                         .createdAt(LocalDateTime.of(2023, 7, 6, 0, 0))
                         .build();
        Long size = 1L;
        String state = "COMPLETE";

        //when
        when(todoMapper.selectTodos(state, size)).thenReturn(List.of(todo));

        //then
        todoService.getTodos(state, size);
        verify(todoMapper).selectTodos(state, size);
    }
}