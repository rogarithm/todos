package kr.rogarithm.todos.todo.dao;

import kr.rogarithm.todos.todo.domain.Todo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public class TodoDao {

    public Todo selectTodoById(Long todoId) {
        return null;
    }
}