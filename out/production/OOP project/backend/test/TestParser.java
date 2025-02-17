package backend.test;

import SyntaxErrorException.SyntaxError;
import backend.parser.ExprTokenizer;
import backend.parser.Tokenizer;
import backend.players.HumanPlayer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class TestParser {

    @Test
    void StatementTest(){

    }

    @Test
    void CommandTest(){

    }

    @Test
    void AssignmentTest(){

    }

    @Test
    void ActionCommandTest(){

    }

    @Test
    void MoveCommandTest(){

    }

    @Test
    void AttackCommand(){

    }

    @Test
    void DirectionTest() {
        HumanPlayer player = new HumanPlayer("PlayerOne");

    }

    @Test
    void BlockTest(){

    }

    @Test
    void IfTest(){

    }

    @Test
    void WhileTest(){

    }

    @Test
    void testTokenizerPeekConsume() throws SyntaxError {
        Tokenizer tokenizer = new ExprTokenizer("cost = 10 ^ (nearby down % 100 + 1)");
        assertTrue(tokenizer.hasNextToken());
        Assert.assertEquals("cost", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("=", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("10", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("^", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("(", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("nearby", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("down", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("%", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("100", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("+", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals("1", tokenizer.peek());
        tokenizer.consume();
        Assert.assertEquals(")", tokenizer.peek());
        tokenizer.consume();
    }
}
