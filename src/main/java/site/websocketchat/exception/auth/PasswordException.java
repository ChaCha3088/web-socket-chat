package site.websocketchat.exception.auth;

public class PasswordException extends RuntimeException {
    public PasswordException(String message) {
        super(message);
    }
}