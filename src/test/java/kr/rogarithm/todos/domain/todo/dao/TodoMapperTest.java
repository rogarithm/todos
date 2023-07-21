package kr.rogarithm.todos.domain.todo.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/schema.sql"})
@SpringBootTest
class TodoMapperTest {

    @Autowired
    TodoMapper todoMapper;

    EasyRandom generator;

    @BeforeEach
    public void setUp() {

        Predicate<Field> todoId = FieldPredicates.named("id")
                                                 .and(FieldPredicates.ofType(Long.class))
                                                 .and(FieldPredicates.inClass(Todo.class));
        Predicate<Field> todoState = FieldPredicates.named("state")
                                                    .and(FieldPredicates.ofType(String.class))
                                                    .and(FieldPredicates.inClass(Todo.class));
        EasyRandomParameters todoParameters = new EasyRandomParameters()
                .randomize(todoId, new LongRangeRandomizer(1L, 100L))
                .randomize(todoState, () -> List.of("INCOMPLETE", "COMPLETE").get(new Random().nextInt(1)));

        generator = new EasyRandom(todoParameters);
    }

    @AfterEach
    public void tearDown() {
        todoMapper.deleteAllTodos();
    }

    @Test
    public void selectTodoFailsWhenIdIsInvalid() {

        Long invalidId = -1L;
        Todo todo = todoMapper.selectTodoById(invalidId);
        assertThat(todo).isNull();
    }

    @Test
    public void selectTodoSuccessWhenIdIsValid() {

        Todo todo = generator.nextObject(Todo.class);
        todoMapper.insertTodo(todo);

        Todo selected = todoMapper.selectTodoById(todo.getId());
        assertThat(selected.getId()).isEqualTo(todo.getId());
    }

    @Test
    public void insertTodoSuccessWhenTodoItemIsValid() {

        Todo todo = generator.nextObject(Todo.class);

        int affected = todoMapper.insertTodo(todo);
        assertThat(affected).isEqualTo(1);
    }

    @Test
    public void selectAllTodos() {

        int size = 0;
        for (int i = 0; i < 10; i++) {
            Todo todo = generator.nextObject(Todo.class);
            todoMapper.insertTodo(todo);
            size++;
        }

        String state = "ALL";
        List<Todo> todos = todoMapper.selectTodos(state, (long) size);
        assertThat(todos.size()).isEqualTo(size);
    }

    @Test
    public void selectCompleteTodos() {

        int size = 0;
        String state = "COMPLETE";

        for (int i = 0; i < 10; i++) {
            Todo todo = generator.nextObject(Todo.class);
            todoMapper.insertTodo(todo);
            if (todo.getState().equals(state)) {
                size++;
            }
        }

        List<Todo> todos = todoMapper.selectTodos(state, (long) size);
        assertThat(todos.size()).isEqualTo(size);
    }

    @Test
    public void selectIncompleteTodos() {

        int size = 0;
        String state = "INCOMPLETE";

        for (int i = 0; i < 10; i++) {
            Todo todo = generator.nextObject(Todo.class);
            todoMapper.insertTodo(todo);
            if (todo.getState().equals(state)) {
                size++;
            }
        }

        List<Todo> todos = todoMapper.selectTodos(state, (long) size);
        assertThat(todos.size()).isEqualTo(size);
    }

    @Test
    @DisplayName("할 일 업데이트 성공")
    public void updateTodoSuccess() {

        Todo todo = generator.nextObject(Todo.class);
        todoMapper.insertTodo(todo);

        Todo updateInfo = generator.nextObject(Todo.class);
        Todo todo2 = Todo.builder()
                         .id(todo.getId())
                         .name(updateInfo.getName())
                         .description(updateInfo.getDescription())
                         .state(updateInfo.getState())
                         .build();

        int affected = todoMapper.updateTodo(todo2);
        assertThat(affected).isEqualTo(1);
    }
}