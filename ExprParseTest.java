import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExprParseTest {

    private ExprParse parser;

    @BeforeEach
    void setUp() {
        Tokenizer tokenizer = new ExprTokenizer("move up");
        tokenizer.setTokens(List.of("move", "up"));
        parser = new ExprParse(tokenizer, new Player() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public int getBudget() {
                return 0;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSumHP() {
                return 0;
            }

            @Override
            public int getRate(int turn) {
                return 0;
            }

            @Override
            public ArrayList<Minion> getMinions() {
                return null;
            }

            @Override
            public ArrayList<HexCell> getHexCells() {
                return null;
            }

            @Override
            public void setBudget(int budget) {

            }

            @Override
            public void buyMinion(Minion minion, boolean isMyHexCell) {

            }

            @Override
            public void buyHexCell(HexCell cell) {

            }

            @Override
            public void takeTurn(int turn) {

            }

            @Override
            public void removeMinion(int index) {

            }

            @Override
            public void setNumber(int i) {

            }

            @Override
            public void setSumHP(int i) {

            }
        }, new Minion() {
            @Override
            public void minionStrategy(String string) {

            }

            @Override
            public int getHP() {
                return 0;
            }

            @Override
            public int getDef() {
                return 0;
            }

            @Override
            public int getX() {
                return 0;
            }

            @Override
            public int getY() {
                return 0;
            }

            @Override
            public int getIndex() {
                return 0;
            }

            @Override
            public Player getOwner() {
                return null;
            }

            @Override
            public void setPosition(int x, int y) {

            }

            @Override
            public void setHP(int hp) {

            }
        });
    }

    @Test
    void testParseMoveCommand() throws IOException {
        Expr expr = parser.parse();
        assertTrue(expr instanceof MoveExpr);
    }

    @Test
    void testParseIfStatement() throws IOException {
        Tokenizer tokenizer = new ExprTokenizer("if (move up) then done else move down");
        tokenizer.setTokens(List.of("if", "(", "move", "up", ")", "then", "done", "else", "move", "down"));
        parser = new ExprParse(tokenizer, new Player() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public int getBudget() {
                return 0;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSumHP() {
                return 0;
            }

            @Override
            public int getRate(int turn) {
                return 0;
            }

            @Override
            public ArrayList<Minion> getMinions() {
                return null;
            }

            @Override
            public ArrayList<HexCell> getHexCells() {
                return null;
            }

            @Override
            public void setBudget(int budget) {

            }

            @Override
            public void buyMinion(Minion minion, boolean isMyHexCell) {

            }

            @Override
            public void buyHexCell(HexCell cell) {

            }

            @Override
            public void takeTurn(int turn) {

            }

            @Override
            public void removeMinion(int index) {

            }

            @Override
            public void setNumber(int i) {

            }

            @Override
            public void setSumHP(int i) {

            }
        }, new Minion() {
            @Override
            public void minionStrategy(String string) {

            }

            @Override
            public int getHP() {
                return 0;
            }

            @Override
            public int getDef() {
                return 0;
            }

            @Override
            public int getX() {
                return 0;
            }

            @Override
            public int getY() {
                return 0;
            }

            @Override
            public int getIndex() {
                return 0;
            }

            @Override
            public Player getOwner() {
                return null;
            }

            @Override
            public void setPosition(int x, int y) {

            }

            @Override
            public void setHP(int hp) {

            }
        });

        Expr expr = parser.parse();
        assertTrue(expr instanceof IfStatementExpr);
    }

    @Test
    void testParseWhileStatement() throws IOException {
        Tokenizer tokenizer = new ExprTokenizer("while (move up) then move down");
        tokenizer.setTokens(List.of("while", "(", "move", "up", ")", "then", "move", "down"));
        parser = new ExprParse(tokenizer, new Player() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public int getBudget() {
                return 0;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSumHP() {
                return 0;
            }

            @Override
            public int getRate(int turn) {
                return 0;
            }

            @Override
            public ArrayList<Minion> getMinions() {
                return null;
            }

            @Override
            public ArrayList<HexCell> getHexCells() {
                return null;
            }

            @Override
            public void setBudget(int budget) {

            }

            @Override
            public void buyMinion(Minion minion, boolean isMyHexCell) {

            }

            @Override
            public void buyHexCell(HexCell cell) {

            }

            @Override
            public void takeTurn(int turn) {

            }

            @Override
            public void removeMinion(int index) {

            }

            @Override
            public void setNumber(int i) {

            }

            @Override
            public void setSumHP(int i) {

            }
        }, new Minion() {
            @Override
            public void minionStrategy(String string) {

            }

            @Override
            public int getHP() {
                return 0;
            }

            @Override
            public int getDef() {
                return 0;
            }

            @Override
            public int getX() {
                return 0;
            }

            @Override
            public int getY() {
                return 0;
            }

            @Override
            public int getIndex() {
                return 0;
            }

            @Override
            public Player getOwner() {
                return null;
            }

            @Override
            public void setPosition(int x, int y) {

            }

            @Override
            public void setHP(int hp) {

            }
        });

        Expr expr = parser.parse();
        assertTrue(expr instanceof WhileStatementExpr);
    }

    @Test
    void testUnexpectedToken() throws IOException {
        Tokenizer tokenizer = new ExprTokenizer("unexpectedToken");
        tokenizer.setTokens(List.of("unexpectedToken"));
        parser = new ExprParse(tokenizer, new Player() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public int getBudget() {
                return 0;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSumHP() {
                return 0;
            }

            @Override
            public int getRate(int turn) {
                return 0;
            }

            @Override
            public ArrayList<Minion> getMinions() {
                return null;
            }

            @Override
            public ArrayList<HexCell> getHexCells() {
                return null;
            }

            @Override
            public void setBudget(int budget) {

            }

            @Override
            public void buyMinion(Minion minion, boolean isMyHexCell) {

            }

            @Override
            public void buyHexCell(HexCell cell) {

            }

            @Override
            public void takeTurn(int turn) {

            }

            @Override
            public void removeMinion(int index) {

            }

            @Override
            public void setNumber(int i) {

            }

            @Override
            public void setSumHP(int i) {

            }
        }, new Minion() {
            @Override
            public void minionStrategy(String string) {

            }

            @Override
            public int getHP() {
                return 0;
            }

            @Override
            public int getDef() {
                return 0;
            }

            @Override
            public int getX() {
                return 0;
            }

            @Override
            public int getY() {
                return 0;
            }

            @Override
            public int getIndex() {
                return 0;
            }

            @Override
            public Player getOwner() {
                return null;
            }

            @Override
            public void setPosition(int x, int y) {

            }

            @Override
            public void setHP(int hp) {

            }
        });

        assertThrows(IOException.class, () -> parser.parse());
    }
}
