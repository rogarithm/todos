package kr.rogarithm.todos.domain.todo.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
import kr.rogarithm.todos.domain.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    TodoController todoController;

    @MockBean
    TodoService todoService;

    @Test
    public void getTodoByIdFailsWhenIdIsInvalid() throws Exception {

        //given
        Long invalidId = -1L;

        //when
        when(todoService.getTodoById(invalidId)).thenThrow(TodoItemNotFoundException.class);

        //then
        this.mockMvc.perform(get("/todo/{todoId}", invalidId))
                    .andDo(print())
                    .andExpect(status().isNotFound());

        verify(todoService).getTodoById(invalidId);
    }

    @Test
    public void getTodoByIdSuccessWhenIdIsValid() throws Exception {

        //given
        Long validId = 1L;

        Todo todoItem = Todo.builder()
                            .id(1L)
                            .name("물 사기")
                            .description("집 앞 슈퍼에서 물 사오기")
                            .state("INCOMPLETE")
                            .createdAt(LocalDateTime.of(2023, 6, 21, 10, 30))
                            .build();

        //when
        when(todoService.getTodoById(validId)).thenReturn(TodoResponse.of(todoItem));

        //then
        this.mockMvc.perform(get("/todo/{todoId}", validId))
                    .andDo(print())
                    .andExpect(status().isOk());

        verify(todoService).getTodoById(validId);
    }

    @Test
    public void addTodoSuccess() throws Exception {

        //given
        AddTodoRequest addTodoRequest = AddTodoRequest.builder()
                                                      .name("물 사기")
                                                      .description("집 앞 슈퍼에서 물 사오기")
                                                      .build();

        String content = objectMapper.writeValueAsString(addTodoRequest);

        //when
        doNothing().when(todoService).saveTodo(addTodoRequest);

        //then
        mockMvc.perform(post("/todo")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());
    }

}