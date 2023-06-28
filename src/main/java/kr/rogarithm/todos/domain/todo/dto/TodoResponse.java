package kr.rogarithm.todos.domain.todo.dto;

import kr.rogarithm.todos.domain.todo.domain.Todo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoResponse {

    private Long id;
    private String name;
    private String description;
    private String state;

    public static TodoResponse of(Todo todo) {

        return TodoResponse.builder()
                           .id(todo.getId())
                           .name(todo.getName())
                           .description(todo.getDescription())
                           .state(todo.getState())
                           .build();
    }
}