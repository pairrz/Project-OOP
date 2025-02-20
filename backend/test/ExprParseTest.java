package backend.test;

import backend.game.*;
import backend.minions.*;
import backend.parser.*;
import backend.players.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

class ExprParseTest {

    GameBoard board = GameBoard.getInstance("PlayerOne", "PlayerTwo");
    Player player = board.getPlayerOne();
    HexCell cell = GameBoard.getHexCell(1, 1);
    Minion minion = new Minion(player, cell);
    Map<String, Integer> bindings = new HashMap<>();

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
        // Start with x = 0, the loop will run until (3 - x) is no longer positive.
        Tokenizer tokenizer = new ExprTokenizer("x = 0 while (3 - x) x = x + 1");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);

        // After three increments, x should be 3
        //assertEquals(3, bindings.get("x"));
    }

    @Test
    void testMoveCommand() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        int budgetBefore = minion.getOwner().getBudget();
        int xBefore = minion.getX();
        int yBefore = minion.getY();

        Tokenizer tokenizer = new ExprTokenizer("move up");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);

        // Budget should decrease by 1
        assertEquals(budgetBefore - 1, minion.getOwner().getBudget());
        // Position should change
        assertNotEquals(xBefore, minion.getX());
        assertNotEquals(yBefore, minion.getY());
    }

    @Test
    void testAttackCommand() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Player player = minion.getOwner();
        HexCell targetCell = GameBoard.getHexCell(1, 2);
        Minion targetMinion = new Minion(board.getPlayerTwo(), targetCell);
        board.buyMinionForPlayerTwo(targetCell, targetMinion);

        Tokenizer tokenizer = new ExprTokenizer("shoot up 3");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        int budgetBefore = player.getBudget();
        int targetHpBefore = targetMinion.getHP();

        expr.eval(bindings);

        // Budget should decrease by 4 (3 cost + 1 base)
        assertEquals(budgetBefore - 4, player.getBudget());
        // HP of target should decrease based on damage calculation
        int expectedHp = Math.max(0, targetHpBefore - Math.max(1, 3 - targetMinion.getDef()));
        assertEquals(expectedHp, targetMinion.getHP());
    }

    @Test
    void testAttackCommand_NotEnoughBudget() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        player.setBudget(3); // Not enough budget for attack
        Tokenizer tokenizer = new ExprTokenizer("shoot up 5"); // Will require budget = 6 (5 + 1)
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        int budgetBefore = player.getBudget();

        expr.eval(bindings);

        // Budget should remain the same because there's not enough to attack
        assertEquals(budgetBefore, player.getBudget());
    }

    @Test
    void testNearbyExpression() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = nearby up");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x");

        // Check if the value returned is correct (distance * HP + DEF + 1 for distance)
        int expectedValue = 100 * 1 + 10 * 1 + 1; // example calculation
        assertEquals(expectedValue, result);
    }

    @Test
    void testNearbyExpression_NoMinion() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = nearby down");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x");

        // If no minion is found in the direction, return 0
        assertEquals(0, result);
    }

    @Test
    void testInfoExpression() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = ally");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        Object result = bindings.get("x");

        // Should return minion of the same player
        assertNotNull(result);
        assertInstanceOf(Minion.class, result);
        assertEquals(player, ((Minion) result).getOwner());
    }
}

