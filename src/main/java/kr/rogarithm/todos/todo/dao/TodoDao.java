package kr.rogarithm.todos.todo.dao;

import kr.rogarithm.todos.todo.domain.Todo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoDao {

    Todo selectTodoById(Long todoId);
}