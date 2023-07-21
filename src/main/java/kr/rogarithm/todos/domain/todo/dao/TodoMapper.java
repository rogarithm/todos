package kr.rogarithm.todos.domain.todo.dao;

import java.util.List;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper {

    Todo selectTodoById(Long todoId);

    List<Todo> selectTodos(String state, Long size);

    int insertTodo(Todo todo);

    int updateTodo(Todo todo);

    void deleteAllTodos();
}