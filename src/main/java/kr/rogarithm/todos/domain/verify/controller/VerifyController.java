package kr.rogarithm.todos.domain.verify.controller;

import kr.rogarithm.todos.domain.verify.dto.VerifyResponse;
import kr.rogarithm.todos.domain.verify.service.VerifyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class VerifyController {

    private final VerifyService verifyService;

    public VerifyController(VerifyService verifyService) {
        this.verifyService = verifyService;
    }

    @GetMapping("/account")
    public ResponseEntity<VerifyResponse> verifyAccount(@RequestParam(name = "account") String account) {

        VerifyResponse response = verifyService.isDuplicatedAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/nickname")
    public ResponseEntity<VerifyResponse> verifyNickname(@RequestParam(name = "nickname") String nickname) {

        VerifyResponse response = verifyService.isDuplicatedNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}