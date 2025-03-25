package backend.game;

public class StrategyProcessingException extends Exception {
    public StrategyProcessingException(String string, Exception e) {
        super(string, e);
    }
}
