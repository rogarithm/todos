package kr.rogarithm.todos.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import kr.rogarithm.todos.domain.user.exception.DuplicateAccountException;
import kr.rogarithm.todos.domain.user.exception.InvalidCompanyRegistrationNumberException;
import kr.rogarithm.todos.domain.user.service.UserService;
import kr.rogarithm.todos.global.auth.JwtAuthenticationFilter;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class)
)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    UserController userController;

    @MockBean
    UserService userService;

    EasyRandom generator;

    JoinUserRequest request;

    @BeforeEach
    public void setUpRequest() {

        generator = new EasyRandom();
        request = generator.nextObject(JoinUserRequest.class);
    }

    @Test
    public void userJoinSuccess() throws Exception {

        //given
        String content = objectMapper.writeValueAsString(request);

        //then
        mockMvc.perform(post("/user")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated());
    }

    @Test
    public void joinFailWhenAccountIsDuplicate() throws Exception {

        //given
        String content = objectMapper.writeValueAsString(request);

        //when
        doThrow(DuplicateAccountException.class)
                .when(userService)
                .registerUser(any(JoinUserRequest.class));

        //then
        mockMvc.perform(post("/user")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict());

        Assertions.assertThrows(DuplicateAccountException.class,
                () -> userService.registerUser(request));
        verify(userService).registerUser(request);
    }

    @Test
    public void joinFailWhenNicknameIsDuplicate() throws Exception {

        //given
        String content = objectMapper.writeValueAsString(request);

        //when
        doThrow(DuplicateAccountException.class)
                .when(userService)
                .registerUser(any(JoinUserRequest.class));

        //then
        mockMvc.perform(post("/user")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict());

        Assertions.assertThrows(DuplicateAccountException.class,
                () -> userService.registerUser(request));
        verify(userService).registerUser(request);
    }

    @Test
    public void joinFailWhenCompanyRegistrationNumberIsInvalid() throws Exception {

        //given
        String content = objectMapper.writeValueAsString(request);

        //when
        doThrow(InvalidCompanyRegistrationNumberException.class)
                .when(userService)
                .registerUser(any(JoinUserRequest.class));

        //then
        mockMvc.perform(post("/user")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict());

        Assertions.assertThrows(InvalidCompanyRegistrationNumberException.class,
                () -> userService.registerUser(request));
        verify(userService).registerUser(request);
    }
}