package kr.rogarithm.todos.domain.todo.dto;

import kr.rogarithm.todos.domain.todo.domain.Todo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddTodoRequest {

    private static final String DEFAULT_TODO_STATE = "INCOMPLETE";

    private String name;
    private String description;

    public Todo toTodo() {

        return Todo.builder()
                   .name(this.name)
                   .description(this.description)
                   .state(DEFAULT_TODO_STATE)
                   .build();
    }
}