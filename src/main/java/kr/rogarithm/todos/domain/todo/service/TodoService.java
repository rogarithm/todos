package kr.rogarithm.todos.domain.todo.service;

import kr.rogarithm.todos.domain.todo.dao.TodoMapper;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoMapper todoMapper;

    public TodoService(TodoMapper todoMapper) {

        this.todoMapper = todoMapper;
    }

    public TodoResponse getTodoById(Long todoId) {

        Todo todo = todoMapper.selectTodoById(todoId);

        if (todo == null) {
            throw new TodoItemNotFoundException("입력한 아이디 " + todoId + "에 해당하는 할 일 항목 정보를 찾을 수 없습니다");
        }

        return TodoResponse.of(todo);
    }

    public void saveTodo(AddTodoRequest addTodoRequest) {

        todoMapper.insertTodo(addTodoRequest.toTodo());
    }
}