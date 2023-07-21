package kr.rogarithm.todos.domain.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import kr.rogarithm.todos.domain.todo.dao.TodoMapper;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.dto.UpdateTodoRequest;
import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.number.LongRandomizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    EasyRandomParameters todoParameters;

    UpdateTodoRequest updateTodoRequest;

    Long id;

    Long size;

    @BeforeEach
    public void setUp() {
        Predicate<Field> todoId = FieldPredicates.named("id")
                                               .and(FieldPredicates.ofType(Long.class))
                                               .and(FieldPredicates.inClass(Todo.class));
        Predicate<Field> todoState = FieldPredicates.named("state")
                                                  .and(FieldPredicates.ofType(String.class))
                                                  .and(FieldPredicates.inClass(Todo.class));
        todoParameters = new EasyRandomParameters()
                .randomize(todoId, new LongRandomizer(10L))
                .randomize(todoState, () -> List.of("INCOMPLETE", "COMPLETE").get(new Random().nextInt(1)));

        generator = new EasyRandom(todoParameters);
        id = generator.nextObject(Long.class);
        size = generator.nextLong(1, 10);
        updateTodoRequest = generator.nextObject(UpdateTodoRequest.class);
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
        String state = "ALL";

        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            todos.add(generator.nextObject(Todo.class));
        }

        //when
        when(todoMapper.selectTodos(state, size)).thenReturn(todos);

        //then
        for (Todo todo : todos) {
            String expected = todo.getState();
            assertThat(expected).isIn("COMPLETE", "INCOMPLETE");
        }
        todoService.getTodos(state, size);
        verify(todoMapper).selectTodos(state, size);
    }

    @Test
    public void getIncompleteTodos() {

        //given
        String state = "INCOMPLETE";

        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Todo todo = generator.nextObject(Todo.class);
            if (todo.getState().equals("INCOMPLETE")) {
                todos.add(todo);
            }
        }

        //when
        when(todoMapper.selectTodos(state, size)).thenReturn(todos);

        //then
        for (Todo todo : todos) {
            String expected = todo.getState();
            assertThat(expected).isEqualTo("INCOMPLETE");
        }
        todoService.getTodos(state, size);
        verify(todoMapper).selectTodos(state, size);
    }

    @Test
    public void getCompleteTodos() {

        //given
        String state = "COMPLETE";

        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Todo todo = generator.nextObject(Todo.class);
            if (todo.getState().equals("COMPLETE")) {
                todos.add(todo);
            }
        }

        //when
        when(todoMapper.selectTodos(state, size)).thenReturn(todos);

        //then
        for (Todo todo : todos) {
            String expected = todo.getState();
            assertThat(expected).isEqualTo("COMPLETE");
        }
        todoService.getTodos(state, size);
        verify(todoMapper).selectTodos(state, size);
    }

    @Test
    @DisplayName("할 일 업데이트 성공")
    public void updateTodoSuccess() {

        //when
        when(todoMapper.selectTodoById(any(Long.class))).thenReturn(generator.nextObject(Todo.class));
        when(todoMapper.updateTodo(any(Todo.class))).thenReturn(1);

        //then
        todoService.updateTodo(updateTodoRequest);
        verify(todoMapper).selectTodoById(any(Long.class));
        verify(todoMapper).updateTodo(any(Todo.class));
    }

    @Test
    @DisplayName("전에 등록했던 할 일이 아니라면 업데이트에 실패")
    public void updateTodoFailWhenTodoNotAddedBefore() {

        //when
        when(todoMapper.selectTodoById(any(Long.class))).thenReturn(null);

        //then
        Assertions.assertThrows(TodoItemNotFoundException.class, () -> todoService.updateTodo(updateTodoRequest));
        verify(todoMapper).selectTodoById(any(Long.class));
    }
}