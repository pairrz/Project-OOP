import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    private Tokenizer tokenizer;

    @BeforeEach
    void setUp() {
        // Initialize Tokenizer with sample input
        tokenizer = new ExprTokenizer("move up");
        tokenizer.setTokens(List.of("move", "up"));
    }

    @Test
    void testHasNextToken() {
        assertTrue(tokenizer.hasNextToken());
    }

    @Test
    void testPeek() {
        assertEquals("move", tokenizer.peek());
    }

    @Test
    void testConsume() {
        assertEquals("move", tokenizer.consume());
        assertEquals("up", tokenizer.peek());
    }

    @Test
    void testConsumeThrowsExceptionWhenNoMoreTokens() {
        tokenizer.consume();
        tokenizer.consume();
        assertThrows(NoSuchElementException.class, tokenizer::consume);
    }
}
