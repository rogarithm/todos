package kr.rogarithm.todos.global.exception;

import java.util.HashMap;
import java.util.Map;
import kr.rogarithm.todos.domain.auth.exception.AuthenticationFailedException;
import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
import kr.rogarithm.todos.domain.user.exception.DuplicateAccountException;
import kr.rogarithm.todos.domain.user.exception.DuplicateNicknameException;
import kr.rogarithm.todos.domain.user.exception.InvalidCompanyRegistrationNumberException;
import kr.rogarithm.todos.domain.verify.dto.VerifyResponse;
import kr.rogarithm.todos.domain.verify.exception.VerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(TodoItemNotFoundException.class)
    protected ResponseEntity<Void> todoItemNotFoundException(TodoItemNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(DuplicateAccountException.class)
    protected ResponseEntity<Void> duplicateAccountException(DuplicateAccountException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    protected ResponseEntity<Void> duplicateNicknameException(DuplicateNicknameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(InvalidCompanyRegistrationNumberException.class)
    protected ResponseEntity<Void> invalidCompanyRegistrationNumberException(
            InvalidCompanyRegistrationNumberException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    protected ResponseEntity<Void> authenticationFailedException(AuthenticationFailedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(VerificationException.class)
    protected ResponseEntity<VerifyResponse> verificationException(VerificationException e) {

        VerifyResponse response = new VerifyResponse(false);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}