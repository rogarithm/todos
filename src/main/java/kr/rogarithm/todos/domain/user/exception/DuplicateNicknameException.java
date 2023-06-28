package kr.rogarithm.todos.domain.user.exception;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException(String message) {
        super(message);
    }
}