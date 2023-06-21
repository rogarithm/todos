package kr.rogarithm.todos.domain.todo.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Todo {

    private Long id;
    private String name;
    private String description;
    private String state;
    private LocalDateTime createdAt;

}