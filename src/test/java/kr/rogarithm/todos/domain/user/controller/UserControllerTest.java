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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    UserController userController;

    @MockBean
    UserService userService;

    @Test
    public void userJoinSuccess() throws Exception {

        JoinUserRequest request = JoinUserRequest.builder()
                                                 .account("sehoongim")
                                                 .password("q1w2e3!")
                                                 .nickname("shrimp-cracker")
                                                 .phone("010-1010-1010")
                                                 .crn("123-45-67890")
                                                 .build();

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/user")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated());
    }

    @Test
    public void joinFailWhenAccountIsDuplicate() throws Exception {

        JoinUserRequest requestWithDuplicateAccount = JoinUserRequest.builder()
                                                                     .account("duplicated")
                                                                     .password("q1w2e3!")
                                                                     .nickname("shrimp-cracker")
                                                                     .phone("010-1010-1010")
                                                                     .crn("123-45-67890")
                                                                     .build();

        String content = objectMapper.writeValueAsString(requestWithDuplicateAccount);

        doThrow(DuplicateAccountException.class)
                .when(userService)
                .registerUser(any(JoinUserRequest.class));

        mockMvc.perform(post("/user")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict());

        Assertions.assertThrows(DuplicateAccountException.class,
                () -> userService.registerUser(requestWithDuplicateAccount));
        verify(userService).registerUser(requestWithDuplicateAccount);
    }

    @Test
    public void joinFailWhenNicknameIsDuplicate() throws Exception {

        JoinUserRequest requestWithDuplicateNickname = JoinUserRequest.builder()
                                                                     .account("sehoongim")
                                                                     .password("q1w2e3!")
                                                                     .nickname("duplicated")
                                                                     .phone("010-1010-1010")
                                                                     .crn("123-45-67890")
                                                                     .build();

        String content = objectMapper.writeValueAsString(requestWithDuplicateNickname);

        doThrow(DuplicateAccountException.class)
                .when(userService)
                .registerUser(any(JoinUserRequest.class));

        mockMvc.perform(post("/user")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict());

        Assertions.assertThrows(DuplicateAccountException.class,
                () -> userService.registerUser(requestWithDuplicateNickname));
        verify(userService).registerUser(requestWithDuplicateNickname);
    }

    @Test
    public void joinFailWhenCompanyRegistrationNumberIsInvalid() throws Exception {

        JoinUserRequest requestWithInvalidCrn = JoinUserRequest.builder()
                                                               .account("sehoongim")
                                                               .password("q1w2e3!")
                                                               .nickname("shrimp-cracker")
                                                               .phone("010-1010-1010")
                                                               .crn("invalidCrn")
                                                               .build();

        String content = objectMapper.writeValueAsString(requestWithInvalidCrn);

        doThrow(InvalidCompanyRegistrationNumberException.class)
                .when(userService)
                .registerUser(any(JoinUserRequest.class));

        mockMvc.perform(post("/user")
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict());

        Assertions.assertThrows(InvalidCompanyRegistrationNumberException.class,
                () -> userService.registerUser(requestWithInvalidCrn));
        verify(userService).registerUser(requestWithInvalidCrn);
    }
}