package chess;

public class CheckMateException extends RuntimeException {
    public CheckMateException(String message) {
        super(message);
    }
}
