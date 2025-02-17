package SyntaxErrorException;

public class DoneException extends Exception {
    public DoneException() {
        super("backend.minions.Minion strategy evaluation ended.");
    }
}