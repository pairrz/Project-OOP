package backend.test;

import backend.parser.ExprTokenizer;
import backend.parser.Tokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    private Tokenizer tokenizer;

    @Test
    void testHasNextToken() {
        tokenizer = new ExprTokenizer("move up");
        assertTrue(tokenizer.hasNextToken());
    }

    @Test
    void testPeek() {
        tokenizer = new ExprTokenizer("move up");
        assertEquals("move", tokenizer.peek());
    }

    @Test
    void testConsume() {
        tokenizer = new ExprTokenizer("move up");
        assertEquals("move", tokenizer.consume());
        assertEquals("up", tokenizer.peek());
    }

    @Test
    void testConsumeThrowsExceptionWhenNoMoreTokens() {
        tokenizer = new ExprTokenizer("move up");
        tokenizer.consume();
        tokenizer.consume();
        assertThrows(NoSuchElementException.class, tokenizer::consume);
    }
}
