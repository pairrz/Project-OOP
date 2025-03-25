package backend.game;

public class InvalidStrategyException extends Exception {
    public InvalidStrategyException(String string, IllegalArgumentException e) {
        super(string, e);
    }
}
