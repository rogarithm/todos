package kr.rogarithm.todos.domain.verify.controller;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    public void successVerifyWhenAccountIsNotDuplicate() throws Exception {

        String account = "sehoongim";

        VerifyResponse response = VerifyResponse.builder()
                                                .verify(true)
                                                .build();

        when(verifyService.isDuplicatedAccount(account)).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/verify/account")
                                             .queryParam("account", account)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = servletResponse.getContentAsString()
                                                    .replaceAll("\\{|\\}|\"", "")
                                                    .split(":")[1];

        assertThat(valueOfResponseBody).isEqualTo(response.getVerify().toString());
        verify(verifyService).isDuplicatedAccount(account);
    }

    @Test
    public void failVerifyWhenAccountIsDuplicate() throws Exception {

        String account = "sehoongim";

        VerifyResponse response = VerifyResponse.builder()
                                                .verify(false)
                                                .build();

        doThrow(VerificationException.class)
                .when(verifyService)
                .isDuplicatedAccount(account);

        MvcResult mvcResult = mockMvc.perform(get("/verify/account")
                                             .queryParam("account", account)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isConflict())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = servletResponse.getContentAsString()
                                                    .replaceAll("\\{|\\}|\"", "")
                                                    .split(":")[1];

        assertThat(valueOfResponseBody).isEqualTo(response.getVerify().toString());
        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedAccount(account));
    }

    @Test
    public void successVerifyWhenNicknameIsNotDuplicate() throws Exception {

        String nickname = "shrimp-cracker";

        VerifyResponse response = VerifyResponse.builder()
                .verify(true)
                .build();

        when(verifyService.isDuplicatedNickname(nickname)).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/verify/nickname")
                                             .queryParam("nickname", nickname)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = servletResponse.getContentAsString()
                                                    .replaceAll("\\{|\\}|\"", "")
                                                    .split(":")[1];

        assertThat(valueOfResponseBody).isEqualTo(response.getVerify().toString());
        verify(verifyService).isDuplicatedNickname(nickname);
    }

    @Test
    public void failVerifyWhenNicknameIsDuplicate() throws Exception {

        String nickname = "shrimp-cracker";

        VerifyResponse response = VerifyResponse.builder()
                                                .verify(false)
                                                .build();

        doThrow(VerificationException.class)
                .when(verifyService)
                .isDuplicatedNickname(nickname);

        MvcResult mvcResult = mockMvc.perform(get("/verify/nickname")
                                             .queryParam("nickname", nickname)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isConflict())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = servletResponse.getContentAsString()
                                                    .replaceAll("\\{|\\}|\"", "")
                                                    .split(":")[1];

        assertThat(valueOfResponseBody).isEqualTo(response.getVerify().toString());
        assertThrows(VerificationException.class, () -> verifyService.isDuplicatedNickname(nickname));
    }

    @Test
    public void successVerifyWhenCrnIsValid() throws Exception {

        String crn = "123-45-67890";

        VerifyResponse response = VerifyResponse.builder()
                                                .verify(true)
                                                .build();

        when(verifyService.isValidCrn(crn)).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/verify/crn")
                                             .queryParam("crn", crn)
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = servletResponse.getContentAsString()
                                                    .replaceAll("\\{|\\}|\"", "")
                                                    .split(":")[1];

        assertThat(valueOfResponseBody).isEqualTo(response.getVerify().toString());
        verify(verifyService).isValidCrn(crn);
    }

    @Test
    public void failVerifyWhenCrnIsInvalid() throws Exception {

        String crn = "123-45-67890";

        doThrow(VerificationException.class)
                .when(verifyService)
                .isValidCrn(crn);

        MvcResult mvcResult = mockMvc.perform(get("/verify/crn")
                                        .queryParam("crn", crn)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isConflict())
                                .andReturn();

        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String valueOfResponseBody = servletResponse.getContentAsString()
                                                    .replaceAll("\\{|\\}|\"", "")
                                                    .split(":")[1];

        assertThat(valueOfResponseBody).isEqualTo("false");
        assertThrows(VerificationException.class, () -> verifyService.isValidCrn(crn));
    }

}