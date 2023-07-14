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
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
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

    EasyRandom generator;

    Long id;

    @BeforeEach
    public void setUp() {
        generator = new EasyRandom();
        id = generator.nextObject(Long.class);
    }

    @Test
    public void validIdShouldReturnTodoItem() {

        //given
        Todo todoItem = generator.nextObject(Todo.class);

        //when
        when(todoMapper.selectTodoById(id)).thenReturn(todoItem);
        TodoResponse todoResponse = todoService.getTodoById(id);

        //then
        verify(todoMapper).selectTodoById(id);
        assertThat(todoResponse).isNotNull();
    }

    @Test
    public void invalidIdShouldThrowException() {

        //when
        when(todoMapper.selectTodoById(id)).thenReturn(null);

        //then
        assertThrows(TodoItemNotFoundException.class,
                () -> todoService.getTodoById(id));
        verify(todoMapper).selectTodoById(id);
    }

    @Test
    public void saveTodoSuccessWhenRequestIsValid() {

        //given
        AddTodoRequest addTodoRequest = generator.nextObject(AddTodoRequest.class);

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