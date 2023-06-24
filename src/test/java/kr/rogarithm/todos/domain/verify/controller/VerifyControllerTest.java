package kr.rogarithm.todos.domain.verify.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kr.rogarithm.todos.domain.verify.dto.VerifyResponse;
import kr.rogarithm.todos.domain.verify.exception.VerificationException;
import kr.rogarithm.todos.domain.verify.service.VerifyService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = VerifyController.class)
class VerifyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    VerifyController verifyController;

    @MockBean
    VerifyService verifyService;

    @Test
    public void successVerifyWhenAccountIsNotDuplicate() throws Exception {

        String account = "sehoongim";

        VerifyResponse response = VerifyResponse.builder()
                                                .verify(true)
                                                .build();

        when(verifyService.isDuplicatedAccount(account)).thenReturn(response);

        mockMvc.perform(get("/verify/account")
                       .queryParam("account", account)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());

        verify(verifyService).isDuplicatedAccount(account);
    }

    @Test
    public void failVerifyWhenAccountIsDuplicate() throws Exception {

        String account = "sehoongim";

        doThrow(VerificationException.class)
                .when(verifyService)
                .isDuplicatedAccount(account);

        mockMvc.perform(get("/verify/account")
                       .queryParam("account", account)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict());

        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedAccount(account));
    }

    }

}