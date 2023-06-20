package kr.rogarithm.todos.todo.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kr.rogarithm.todos.todo.dto.TodoResponse;
import kr.rogarithm.todos.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    TodoController todoController;

    @MockBean
    TodoService todoService;

    @Test
    public void getTodoByIdSuccess() throws Exception {
        Long todoId = 1L;

        when(todoService.getTodoById(todoId)).thenReturn(
                new TodoResponse(1L, "물 사기", "집 앞 슈퍼에서 물 사오기", "INCOMPLETE")
        );

        this.mockMvc.perform(get("/todos/{todoId}", todoId))
                    .andDo(print())
                    .andExpect(status().isOk());

        verify(todoService).getTodoById(todoId);
    }
}