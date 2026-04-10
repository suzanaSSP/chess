package chess;

public class StalemateException extends RuntimeException {
    public StalemateException(String message) {
        super(message);
    }
}
