package kr.rogarithm.todos.domain.todo.dto;

import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import lombok.Getter;

@Getter
public class UpdateTodoRequest {

    @Min(1L)
    private Long id;
    @NotBlank(message = "할일 제목이 입력되지 않았습니다")
    private String name;
    private String description;
    @Pattern(regexp = "ALL|INCOMPLETE|COMPLETE")
    private String state;

    public Todo toTodo() {

        return Todo.builder()
                   .id(this.id)
                   .name(this.name)
                   .description(this.description)
                   .state(this.state)
                   .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateTodoRequest that = (UpdateTodoRequest) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(description, that.description)
                && state.equals(that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, state);
    }
}