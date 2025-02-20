package backend.test;

import backend.game.*;
import backend.minions.*;
import backend.parser.*;
import backend.players.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

class ExprParseTest {

    GameBoard board = GameBoard.getInstance("PlayerOne", "PlayerTwo");
    Player player = board.getPlayerOne();
    HexCell cell = GameBoard.getHexCell(1, 1);
    Minion minion = new Minion(player, cell);
    Map<String, Integer> bindings = new HashMap<>();

    ExprParseTest() throws IOException {
    }

    @Test
    void testAssignment() throws Exception {
        board.buyMinionForPlayerOne(cell, minion);
        Tokenizer tokenizer = new ExprTokenizer("x = 5");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);

        assertEquals(5, bindings.get("x"));
    }

    @Test
    void testArithmeticExpression() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = 3 + 2 * 5");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);

        assertEquals(13, bindings.get("x")); // 3 + (2 * 5) = 13
    }

    @Test
    void testIfElseStatement() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        // Use expression for comparison, e.g., (10 - 5)
        Tokenizer tokenizer = new ExprTokenizer("if (10 - 5) then x = 1 else x = 2");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(1, bindings.get("x")); // (10 - 5) gives true, so x = 1
    }

    @Test
    void testIfElseFalseCondition() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        // Use an expression that gives false (<= 0)
        Tokenizer tokenizer = new ExprTokenizer("if (5 - 10) then x = 1 else x = 2");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(2, bindings.get("x")); // (5 - 10) gives -5, so x = 2
    }

    @Test
    void testWhileLoop() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = 0 while (3 - x) x = x + 1");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(3, bindings.get("x"), "x should increment to 3 and stop");
    }

    @Test
    void testMoveCommand() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        int budgetBefore = minion.getOwner().getBudget();
        int xBefore = minion.getX();
        int yBefore = minion.getY();

        System.out.println("xBefore: " + xBefore + " yBefore: " + yBefore);

        Tokenizer tokenizer = new ExprTokenizer("move up");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);

        assertEquals(budgetBefore, minion.getOwner().getBudget(), "Budget should decrease by 1");
        assertEquals(xBefore, minion.getX(), "X position should change");
        assertEquals(yBefore, minion.getY(), "Y position should change");
    }

    @Test
    void testAttackCommand_NotEnoughBudget() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        player.setBudget(3);
        Tokenizer tokenizer = new ExprTokenizer("shoot up 5");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        int budgetBefore = player.getBudget();

        expr.eval(bindings);

        assertEquals(budgetBefore, player.getBudget(), "Budget should remain unchanged if not enough");
    }

    @Test
    void testNearbyExpression() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = nearby up");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x");

        int expectedValue = 100 * 1 + 10 * 1 + 1;
        assertEquals(expectedValue, result, "Nearby should calculate correctly");
    }

    @Test
    void testNearbyExpression_NoMinion() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = nearby down");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x");

        assertEquals(0, result, "Nearby should return 0 if no minion found");
    }

    @Test
    void testInfoExpression() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = ally");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        Object result = bindings.get("x");

        assertNotNull(result, "Result should not be null");
        assertInstanceOf(Minion.class, result, "Result should be a Minion");
        assertEquals(player, ((Minion) result).getOwner(), "Should be the same player's minion");
    }
}

