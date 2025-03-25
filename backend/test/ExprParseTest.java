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
    Player opponent;
    HexCell cell;
    HexCell targetCell;
    Minion minion;
    Minion targetMinion;
    Map<String, Integer> bindings = new HashMap<>();

    ExprParseTest() throws IOException {
        try {
            FileProcess file = new FileProcess();
            file.readConfig("D:\\OOP project\\backend\\Configuration");
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
        }

        this.board = GameBoard.getInstance("one","two");

        this.player = board.getPlayerOne();
        this.cell = GameBoard.getHexCell(0, 0);
        board.buyMinionForPlayerOne(cell);
        this.minion  = new Minion(player, cell);


        this.opponent = board.getPlayerTwo();
        this.targetCell = GameBoard.getHexCell(7, 7); // อยู่ด้านบน
        board.buyMinionForPlayerTwo(targetCell);
        this.targetMinion = new Minion(opponent, targetCell);

    }

    @Test
    void testAssignment() throws Exception {
        board.buyMinionForPlayerOne(cell);
        Tokenizer tokenizer = new ExprTokenizer("x = 5");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);

        assertEquals(5, bindings.get("x"));
    }

    @Test
    void testArithmeticExpression() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer("x = 3 ^ 2 % 5");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(4, bindings.get("x")); // 3 ^ 2 % 5 = 4
    }

    @Test
    void testIfElseStatement() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer("if (10 - 5) then x = 1 else x = 2");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(1, bindings.get("x")); // (10 - 5) เป็นจริง -> x = 1
    }

    @Test
    void testIfElseFalseCondition() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer("if (5 - 10) then x = 1 else x = 2");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(2, bindings.get("x")); // (5 - 10) ได้ -5 เป็นเท็จ -> x = 2
    }

    @Test
    void testNestedIfElseStatement() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer(
                "if (10 - 5) then " +
                        "   if (20 - 15) then " +
                        "       x = 1 " +
                        "   else " +
                        "       x = 2 " +
                        "else " +
                        "   x = 3"
        );
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(1, bindings.get("x")); // (10 - 5) และ (20 - 15) เป็นจริง -> x = 1
    }

    @Test
    void testDeepNestedIfElse() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer(
                "if (10 - 5) then " +
                        "   if (20 - 10) then " +
                        "       if (30 - 30) then " +
                        "           x = 1 " +
                        "       else " +
                        "           x = 2 " +
                        "   else " +
                        "       x = 3 " +
                        "else " +
                        "   x = 4"
        );
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(2, bindings.get("x")); // (10-5) และ (20-10) เป็นจริง, (30-30) เป็นเท็จ → x = 2
    }

    @Test
    void testIfElseWithVariable() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer(
                "x = 5 " +
                        "if (x - 3) then " +
                        "   if (x - 5) then " +
                        "       y = 1 " +
                        "   else " +
                        "       y = 2 " +
                        "else " +
                        "   y = 3"
        );
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(2, bindings.get("y")); // x = 5 → (x - 3) เป็นจริง → (x - 5) เป็นเท็จ → y = 2
    }

    @Test
    void testWhileLoop() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer("x = 0 while (3 - x) { x = x + 1 }");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        assertEquals(3, bindings.get("x")); // x = 3
    }

    @Test
    void testAttackCommand_NotEnoughBudget() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer("shoot down 5");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        int budgetAfter = player.getBudget() - 6;

        expr.eval(bindings);

        assertEquals(budgetAfter, player.getBudget());
    }

    @Test
    void testNearbyExpression() throws Exception {
        System.out.println(opponent.getName());
        HexCell testCell = GameBoard.getHexCell(4, 7); //อยู่ขวาล่าง
        Minion testMinion = new Minion(opponent, testCell);

        System.out.println(minion.getX() + " " + minion.getY());
        System.out.println(testMinion.getX() + " " + testMinion.getY());

        Tokenizer tokenizer = new ExprTokenizer("x = nearby downright");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x");

        int expectedValue = 100 * 3 + 10 * 2 + 7;
        assertEquals(expectedValue, result);
    }

    @Test
    void testNearbyExpression_NoMinion() throws Exception {
        Tokenizer tokenizer = new ExprTokenizer("x = nearby upleft");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.get("x");

        assertEquals(0, result);
    }

    @Test
    void testInfoExpression() throws Exception {
        HexCell testCell = GameBoard.getHexCell(4, 7);
        Minion testMin = new Minion(opponent, testCell);

        Tokenizer tokenizer = new ExprTokenizer("x = opponent");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);
        int result = bindings.getOrDefault("x", -1);  // ป้องกัน null

        System.out.println("Test result: " + result);

        assertEquals(73, result);
    }

    @Test
    void testMoveCommand() throws Exception {
        int budgetBefore = minion.getOwner().getBudget() - 1;

        System.out.println(budgetBefore);

        int xAfter = minion.getX() + 1;
        int yAfter = minion.getY();

        System.out.println("xBefore: " + xAfter + " yBefore: " + yAfter);

        Tokenizer tokenizer = new ExprTokenizer("move down");
        Parser parser = new ExprParse(tokenizer, minion);
        Expr expr = parser.parse();

        expr.eval(bindings);

        System.out.println(minion.getX() + " " + minion.getY());

        assertEquals(budgetBefore, minion.getOwner().getBudget());
        assertEquals(xAfter, minion.getX());
        assertEquals(yAfter, minion.getY());
    }

    @Test
    void allTest() throws Exception {
        System.out.println(GameConfig.InitBudget);

        System.out.println(minion.getX() + " " + minion.getY());
        System.out.println("status one :" + " " + player.getName() + " " + player.getBudget());

        System.out.println(targetMinion.getX() + " " + targetMinion.getY());
        System.out.println("status two :" + " " + opponent.getName() + " " + opponent.getBudget());

        board.showBoard();
        StrategyReader str = new StrategyReader();

        FileProcess file = new FileProcess();
        str.readStrategyFromFile("D:\\OOP project\\backend\\strategy\\Strategy3.txt",minion);

        board.showBoard();

        str.readStrategyFromFile("D:\\OOP project\\backend\\strategy\\Strategy3.txt",targetMinion);

        board.showBoard();
    }

    @Test
    void strategyBaseTest() throws Exception {
        StrategyReader file = new StrategyReader();

        try {
            file.readStrategyFromFile("D:\\OOP project\\backend\\strategy\\Strategy3.txt", minion);
        } catch (IOException e) {
            fail("Failed to read strategy file: " + e.getMessage());
        }

    }

    @Test
    void strategyTestPass() throws Exception {
        StrategyReader file = new StrategyReader();

        file.readStrategyFromString("cost = 30 if(cost) then shoot down cost", minion);
    }

    @Test
    void strategyTestFailed() {
        HexCell cell = new HexCell(1,0);
        Minion minForTest = new Minion(player,cell);

        String strategy = "cost = 30; if(cost) shoot down cost";

        StrategyReader file = new StrategyReader();
        Exception exception = assertThrows(StrategyProcessingException.class, () -> {
            file.readStrategyFromString(strategy, minForTest);
        });

        String actualMessage = exception.getMessage();
        assertNotNull(actualMessage, "The exception message should not be null");

        String expectedMessage = "Error processing strategy";
        assertTrue(actualMessage.contains(expectedMessage), "The error message should contain: " + expectedMessage);
    }
}
