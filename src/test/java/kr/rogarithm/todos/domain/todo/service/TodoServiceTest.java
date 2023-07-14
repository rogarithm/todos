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
import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.number.LongRandomizer;
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

    EasyRandomParameters todoParameters;

    Long id;

    class StateRandomizer implements Randomizer<String> {

        private final List<String> states = List.of("INCOMPLETE", "COMPLETE");

        @Override
        public String getRandomValue() {
            return states.get(new Random().nextInt(1));
        }
    }

    @BeforeEach
    public void setUp() {
        Predicate<Field> isId = FieldPredicates.named("id")
                                               .and(FieldPredicates.ofType(Long.class))
                                               .and(FieldPredicates.inClass(Todo.class));
        Predicate<Field> isState = FieldPredicates.named("state")
                                                  .and(FieldPredicates.ofType(String.class))
                                                  .and(FieldPredicates.inClass(Todo.class));
        todoParameters = new EasyRandomParameters()
                .randomize(isId, new LongRandomizer(10L))
                .randomize(isState, new StateRandomizer());

        generator = new EasyRandom(todoParameters);
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
        Long size = generator.nextLong(1, 10);
        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            todos.add(generator.nextObject(Todo.class));
        }
        String state = "ALL";

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
        Long size = generator.nextLong(1, 10);
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
        Long size = generator.nextLong(1, 10);
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
}