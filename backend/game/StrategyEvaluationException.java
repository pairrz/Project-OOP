package backend.game;

public class StrategyEvaluationException extends Exception {
    public StrategyEvaluationException(String string, ArithmeticException e) {
        super(string, e);
    }
}
