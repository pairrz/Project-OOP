package backend.parser;

import java.io.IOException;

public interface Parser {
    Expr parse() throws IOException;
}
