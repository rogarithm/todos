package kr.rogarithm.todos.todo.dto;

import kr.rogarithm.todos.todo.domain.Todo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoResponse {

    /**
     * state
     * ALL : 완료된 항목과 미완료된 항목 모두
     * COMPLETE : 완료된 항목만
     * INCOMPLETE : 미완료된 항목만
     */
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