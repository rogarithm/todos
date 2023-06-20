package kr.rogarithm.todos.todo.dto;

import lombok.Getter;

@Getter
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

    public TodoResponse(Long id, String name, String description, String state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
    }
}