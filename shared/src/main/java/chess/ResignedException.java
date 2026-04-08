package chess;

public class ResignedException extends RuntimeException {
    public ResignedException(String message) {
        super(message);
    }
    public ResignedException() {}
}
