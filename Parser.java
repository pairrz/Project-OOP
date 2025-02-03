import SyntaxErrorException.SyntaxError;

public interface Parser {
    public Expr parse() throws SyntaxError;
}
