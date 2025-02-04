import java.util.Map;

public interface Tokenizer {
    boolean hasNextToken();
    String peek();
    String consume();
    boolean peek(String s);
    void consume(String s);
}