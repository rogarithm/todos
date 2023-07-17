package kr.rogarithm.todos.domain.todo.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.dto.UpdateTodoRequest;
import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
import kr.rogarithm.todos.domain.todo.service.TodoService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
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

    EasyRandom generator;

    AddTodoRequest addTodoRequest;

    UpdateTodoRequest updateTodoRequest;

    Long id;

    Todo todo;

    @BeforeEach
    public void setUp() {
        generator = new EasyRandom();
        addTodoRequest = generator.nextObject(AddTodoRequest.class);
        updateTodoRequest = generator.nextObject(UpdateTodoRequest.class);
        id = generator.nextObject(Long.class);
        todo = generator.nextObject(Todo.class);
    }

    @Test
    public void getTodoByIdFailsWhenIdIsInvalid() throws Exception {

        //when
        when(todoService.getTodoById(id)).thenThrow(TodoItemNotFoundException.class);

        //then
        this.mockMvc.perform(get("/todo/{todoId}", id))
                    .andDo(print())
                    .andExpect(status().isNotFound());

        verify(todoService).getTodoById(id);
    }

    @Test
    public void getTodoByIdSuccessWhenIdIsValid() throws Exception {

        //when
        when(todoService.getTodoById(id)).thenReturn(TodoResponse.of(todo));

        //then
        this.mockMvc.perform(get("/todo/{todoId}", id))
                    .andDo(print())
                    .andExpect(status().isOk());

        verify(todoService).getTodoById(id);
    }

    @Test
    public void addTodoSuccess() throws Exception {

        //given
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

        verify(todoService).saveTodo(addTodoRequest);
    }

    @Test
    public void getTodoListNoSpecifyStateSuccess() throws Exception {

        //given
        Long size = 2L;
        String state = "ALL";
        TodoResponse todo1 = TodoResponse.builder()
                                         .id(1L)
                                         .name("커피 원두 구입")
                                         .description("디벨로핑룸 가서 커피 원두 사기")
                                         .state("COMPLETE")
                                         .build();
        TodoResponse todo2 = TodoResponse.builder()
                                         .id(2L)
                                         .name("회덮밥 사오기")
                                         .description("바다회 사랑 가서 회덮밥 포장해오기")
                                         .state("INCOMPLETE")
                                         .build();

        //when
        when(todoService.getTodos(state, size)).thenReturn(List.of(todo1, todo2));

        //then
        mockMvc.perform(get("/todo")
                       .queryParam("state", state)
                       .queryParam("size", size.toString()))
               .andDo(print())
               .andExpect(jsonPath("$[0].id").value("1"))
               .andExpect(jsonPath("$[0].name").value("커피 원두 구입"))
               .andExpect(jsonPath("$[1].id").value("2"))
               .andExpect(jsonPath("$[1].name").value("회덮밥 사오기"))
               .andExpect(status().isOk());

        verify(todoService).getTodos(state, size);
    }

    @Test
    public void getTodoListCompleteStateOnlySuccess() throws Exception {

        //given
        Long size = 1L;
        String state = "COMPLETE";
        TodoResponse todo1 = TodoResponse.builder()
                                         .id(2L)
                                         .name("회덮밥 사오기")
                                         .description("바다회 사랑 가서 회덮밥 포장해오기")
                                         .state("COMPLETE")
                                         .build();
        TodoResponse todo2 = TodoResponse.builder()
                                         .id(2L)
                                         .name("회덮밥 사오기")
                                         .description("바다회 사랑 가서 회덮밥 포장해오기")
                                         .state("INCOMPLETE")
                                         .build();

        //when
        when(todoService.getTodos(state, size)).thenReturn(List.of(todo1));

        //then
        mockMvc.perform(get("/todo")
                       .queryParam("state", state)
                       .queryParam("size", size.toString()))
               .andDo(print())
               .andExpect(jsonPath("$[0].state").value("COMPLETE"))
               .andExpect(status().isOk());

        verify(todoService).getTodos(state, size);
    }

    @Test
    public void getTodoListIncompleteStateOnlySuccess() throws Exception {

        //given
        Long size = 1L;
        String state = "INCOMPLETE";
        TodoResponse todo1 = TodoResponse.builder()
                                         .id(2L)
                                         .name("회덮밥 사오기")
                                         .description("바다회 사랑 가서 회덮밥 포장해오기")
                                         .state("COMPLETE")
                                         .build();
        TodoResponse todo2 = TodoResponse.builder()
                                         .id(2L)
                                         .name("회덮밥 사오기")
                                         .description("바다회 사랑 가서 회덮밥 포장해오기")
                                         .state("INCOMPLETE")
                                         .build();

        //when
        when(todoService.getTodos(state, size)).thenReturn(List.of(todo2));

        //then
        mockMvc.perform(get("/todo")
                       .queryParam("state", state)
                       .queryParam("size", size.toString()))
               .andDo(print())
               .andExpect(jsonPath("$[0].state").value("INCOMPLETE"))
               .andExpect(status().isOk());

        verify(todoService).getTodos(state, size);
    }

    @Test
    public void updateTodoSuccessWhenValidRequest() throws Exception {

        //given
        String content = objectMapper.writeValueAsString(updateTodoRequest);

        //when
        doNothing().when(todoService).updateTodo(updateTodoRequest);

        //then
        mockMvc.perform(put("/todo")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());

        verify(todoService).updateTodo(updateTodoRequest);
    }
}