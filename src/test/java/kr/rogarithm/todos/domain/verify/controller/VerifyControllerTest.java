package kr.rogarithm.todos.domain.verify.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import kr.rogarithm.todos.domain.verify.dto.VerifyResponse;
import kr.rogarithm.todos.domain.verify.exception.VerificationException;
import kr.rogarithm.todos.domain.verify.service.VerifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = VerifyController.class)
class VerifyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    VerifyController verifyController;

    @MockBean
    VerifyService verifyService;

    String account;

    String nickname;

    String crn;

    VerifyResponse responseBodyWhenSuccess;

    VerifyResponse responseBodyWhenFail;

    @BeforeEach
    public void setUpGiven() {

        //given
        account = "sehoongim";
        nickname = "shrimp-cracker";
        crn = "123-45-67890";
        responseBodyWhenSuccess = new VerifyResponse(true);
        responseBodyWhenFail = new VerifyResponse(false);
    }

    @Test
    public void successVerifyWhenAccountIsNotDuplicate() throws Exception {

        //when
        when(verifyService.isDuplicatedAccount(account)).thenReturn(responseBodyWhenSuccess);

        //then
        MvcResult mvcResult = mockMvc.perform(get("/verify/account")
                                             .queryParam("account", account)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = extractValueFrom(servletResponse);

        assertThat(valueOfResponseBody).isEqualTo(responseBodyWhenSuccess.getVerify().toString());
        verify(verifyService).isDuplicatedAccount(account);
    }

    @Test
    public void failVerifyWhenAccountIsDuplicate() throws Exception {

        //when
        doThrow(VerificationException.class)
                .when(verifyService)
                .isDuplicatedAccount(account);

        //then
        MvcResult mvcResult = mockMvc.perform(get("/verify/account")
                                             .queryParam("account", account)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isConflict())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = extractValueFrom(servletResponse);

        assertThat(valueOfResponseBody).isEqualTo(responseBodyWhenFail.getVerify().toString());
        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedAccount(account));
    }

    @Test
    public void successVerifyWhenNicknameIsNotDuplicate() throws Exception {

        //when
        when(verifyService.isDuplicatedNickname(nickname)).thenReturn(responseBodyWhenSuccess);

        //then
        MvcResult mvcResult = mockMvc.perform(get("/verify/nickname")
                                             .queryParam("nickname", nickname)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = extractValueFrom(servletResponse);

        assertThat(valueOfResponseBody).isEqualTo(responseBodyWhenSuccess.getVerify().toString());
        verify(verifyService).isDuplicatedNickname(nickname);
    }

    @Test
    public void failVerifyWhenNicknameIsDuplicate() throws Exception {

        //when
        doThrow(VerificationException.class)
                .when(verifyService)
                .isDuplicatedNickname(nickname);

        //then
        MvcResult mvcResult = mockMvc.perform(get("/verify/nickname")
                                             .queryParam("nickname", nickname)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isConflict())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = extractValueFrom(servletResponse);

        assertThat(valueOfResponseBody).isEqualTo(responseBodyWhenFail.getVerify().toString());
        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedNickname(nickname));
    }

    @Test
    public void successVerifyWhenCrnIsValid() throws Exception {

        //when
        when(verifyService.isValidCrn(crn)).thenReturn(responseBodyWhenSuccess);

        //then
        MvcResult mvcResult = mockMvc.perform(get("/verify/crn")
                                             .queryParam("crn", crn)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = extractValueFrom(servletResponse);

        assertThat(valueOfResponseBody).isEqualTo(responseBodyWhenSuccess.getVerify().toString());
        verify(verifyService).isValidCrn(crn);
    }

    @Test
    public void failVerifyWhenCrnIsInvalid() throws Exception {

        //when
        doThrow(VerificationException.class)
                .when(verifyService)
                .isValidCrn(crn);

        //then
        MvcResult mvcResult = mockMvc.perform(get("/verify/crn")
                                             .queryParam("crn", crn)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isConflict())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = extractValueFrom(servletResponse);

        assertThat(valueOfResponseBody).isEqualTo(responseBodyWhenFail.getVerify().toString());
        assertThrows(VerificationException.class, () -> verifyService.isValidCrn(crn));
    }

    private String extractValueFrom(MockHttpServletResponse keyAndValue) throws UnsupportedEncodingException {

        return keyAndValue.getContentAsString()
                          .replaceAll("\\{|\\}|\"", "")
                          .split(":")[1];
    }
}