package kr.rogarithm.todos.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.rogarithm.todos.domain.user.dto.JoinUserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
}