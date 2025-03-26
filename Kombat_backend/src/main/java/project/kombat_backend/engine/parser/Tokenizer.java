package project.kombat_backend.engine.parser;

import java.io.IOException;

public interface Tokenizer {
    boolean hasNextToken();
    String peek();
    String consume() throws IOException;
    boolean peek(String s);
    void consume(String s) throws IOException;
}