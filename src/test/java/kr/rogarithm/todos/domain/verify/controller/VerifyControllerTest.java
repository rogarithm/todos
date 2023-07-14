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
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
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

    EasyRandom generator;

    VerifyResponse response;

    String account;

    String nickname;

    String crn;

    @BeforeEach
    public void setUpGiven() {

        generator = new EasyRandom();
        response = generator.nextObject(VerifyResponse.class);
        account = generator.nextObject(String.class);
        nickname =  generator.nextObject(String.class);
        crn = generator.nextObject(String.class);
    }

    @Test
    public void successVerifyWhenAccountIsNotDuplicate() throws Exception {

        //when
        when(verifyService.isDuplicatedAccount(account)).thenReturn(response);

        //then
        mockMvc.perform(get("/verify/account")
                       .queryParam("account", account)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andReturn();

        verify(verifyService).isDuplicatedAccount(account);
    }

    @Test
    public void failVerifyWhenAccountIsDuplicate() throws Exception {

        //when
        doThrow(VerificationException.class)
                .when(verifyService)
                .isDuplicatedAccount(account);

        //then
        mockMvc.perform(get("/verify/account")
                       .queryParam("account", account)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict())
               .andReturn();

        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedAccount(account));
    }

    @Test
    public void successVerifyWhenNicknameIsNotDuplicate() throws Exception {

        //when
        when(verifyService.isDuplicatedNickname(nickname)).thenReturn(response);

        //then
        mockMvc.perform(get("/verify/nickname")
                       .queryParam("nickname", nickname)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andReturn();

        verify(verifyService).isDuplicatedNickname(nickname);
    }

    @Test
    public void failVerifyWhenNicknameIsDuplicate() throws Exception {

        //when
        doThrow(VerificationException.class)
                .when(verifyService)
                .isDuplicatedNickname(nickname);

        //then
        mockMvc.perform(get("/verify/nickname")
                       .queryParam("nickname", nickname)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict())
               .andReturn();

        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedNickname(nickname));
    }

    @Test
    public void successVerifyWhenCrnIsValid() throws Exception {

        //when
        when(verifyService.isValidCrn(crn)).thenReturn(response);

        //then
        mockMvc.perform(get("/verify/crn")
                       .queryParam("crn", crn)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andReturn();

        verify(verifyService).isValidCrn(crn);
    }

    @Test
    public void failVerifyWhenCrnIsInvalid() throws Exception {

        //when
        doThrow(VerificationException.class)
                .when(verifyService)
                .isValidCrn(crn);

        //then
        mockMvc.perform(get("/verify/crn")
                       .queryParam("crn", crn)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isConflict())
               .andReturn();

        assertThrows(VerificationException.class, () -> verifyService.isValidCrn(crn));
    }
}