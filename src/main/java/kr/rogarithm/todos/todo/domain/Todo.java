package kr.rogarithm.todos.todo.domain;

import java.time.LocalDateTime;

public class Todo {

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
    private LocalDateTime createdAt;
}