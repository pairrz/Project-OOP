import java.io.IOException;

public interface Parser {
    public Expr parse() throws IOException;
}
