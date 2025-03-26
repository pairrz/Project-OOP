package project.kombat_backend.engine.exception;

public class SyntaxError extends RuntimeException {
    public SyntaxError() {}
    public SyntaxError(String message) {
        super(message);
    }
}
