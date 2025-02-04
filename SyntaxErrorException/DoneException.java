package SyntaxErrorException;

public class DoneException extends Exception {
    public DoneException() {
        super("Minion strategy evaluation ended.");
    }
}