package backend.test;

import backend.parser.ExprTokenizer;
import backend.parser.Tokenizer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    private Tokenizer tokenizer;

    @Test
    void testHasNextToken() throws IOException {
        tokenizer = new ExprTokenizer("move up");
        assertTrue(tokenizer.hasNextToken());
    }

    @Test
    void testPeek() throws IOException {
        tokenizer = new ExprTokenizer("move up");
        assertEquals("move", tokenizer.peek());
    }

    @Test
    void testConsume() throws IOException {
        tokenizer = new ExprTokenizer("move up");
        assertEquals("move", tokenizer.consume());
        assertEquals("up", tokenizer.peek());
    }

    @Test
    void testConsumeThrowsExceptionWhenNoMoreTokens() throws IOException {
        tokenizer = new ExprTokenizer("move up");
        tokenizer.consume();
        tokenizer.consume();
        assertThrows(NoSuchElementException.class, tokenizer::consume);
    }
}
