package kr.rogarithm.todos.domain.user.exception;

public class InvalidCompanyRegistrationNumberException extends RuntimeException {

    public InvalidCompanyRegistrationNumberException(String message) {
        super(message);
    }
}