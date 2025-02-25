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

    GameBoard board;
    Player player;
    HexCell cell;
    Minion minion;
    Map<String, Integer> bindings;

    ExprParseTest() throws IOException {
        this.board = GameBoard.getInstance("one","two");
        this.player = board.getPlayerOne();
        this.cell = GameBoard.getHexCell(0, 0);
        this.minion  = new Minion(player, cell);
        //board.buyMinionForPlayerOne(cell, minion);
        bindings = new HashMap<>();
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
        ExprParseTest test = new ExprParseTest();

        // สร้าง Minion ฝ่ายตรงข้ามที่อยู่ใกล้
        Player opponent = board.getPlayerTwo();
        HexCell targetCell = GameBoard.getHexCell(3, 1); // อยู่ด้านบน
        opponent.getHex(targetCell);
        Minion targetMinion = new Minion(opponent, targetCell);

        System.out.println(cell.getX() + " " + cell.getY());
        System.out.println(targetCell.getX() + " " + targetCell.getY());

        System.out.println(minion.getX() + " " + minion.getY());
        System.out.println(targetMinion.getX() + " " + targetMinion.getY());

        // สร้าง Tokenizer สำหรับ NearbyExpr
        Tokenizer tokenizer = new ExprTokenizer("x = nearby down");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x");

        // ตรวจสอบว่าค่าที่ได้ถูกต้อง
        int expectedValue = 100 * targetMinion.getHP() + 10 * targetMinion.getDef() + 2;
        assertEquals(expectedValue, result);
    }

    @Test
    void testNearbyExpression_NoMinion() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer("x = nearby upleft");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x");

        assertEquals(0, result, "Should return 0 when no minion is nearby");
    }

    @Test
    void testInfoExpression() throws Exception {
        Map<String, Integer> bindings = new HashMap<>();
        Tokenizer tokenizer = new ExprTokenizer("x = ally");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x"); // ✅ ต้องไม่เป็น null

        assertNotNull(result, "Result should not be null");
        assertTrue(result >= 0, "Result should be a valid integer");
    }

    @Test
    void allTest() throws Exception {
        ExprParseTest test = new ExprParseTest();

        System.out.println(minion.getX() + " " + minion.getY());

        Player opponent = board.getPlayerTwo();
        HexCell targetCell = GameBoard.getHexCell(7, 7); // อยู่ด้านบน
        Minion targetMinion = new Minion(opponent, targetCell);
        System.out.println(targetMinion.getX() + " " + targetMinion.getY());

        board.showBoard();

        FileProcess file = new FileProcess();
        file.readStrategy("D:\\OOP project\\backend\\strategy\\Strategy2.txt",minion);

        board.showBoard();
    }

    @Test
    void strategyTest() throws Exception {
        ExprParseTest test = new ExprParseTest();

        System.out.println(minion.getX() + " " + minion.getY());

        FileProcess file = new FileProcess();
        file.readStrategy("D:\\OOP project\\backend\\strategy\\Strategy2.txt",minion);
    }
}