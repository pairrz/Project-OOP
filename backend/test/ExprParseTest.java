//package backend.test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import backend.evaluation.*;
//import backend.game.*;
//import backend.minions.*;
//import backend.parser.*;
//import backend.players.*;
//import org.junit.jupiter.api.Test;
//import java.io.IOException;
//
//class ExprParserTest {
//
//    private Parser createParser(String input) {
//        Tokenizer token = new ExprTokenizer(input);
//        HexCell hex = new HexCell(1, 1);
//        Player player = new HumanPlayer("player");
//        Minion minion = new HumanMinion("minion", hex);
//        return new ExprParse(token, player, minion);
//    }
//
//    @Test
//    void testParseMoveCommand() throws IOException {
//        Parser parser = createParser("move down");
//        Expr expr = parser.parse();
//
//        assertAll(
//                () -> assertNotNull(expr, "Parsed expression should not be null"),
//                () -> assertInstanceOf(MoveExpr.class, expr, "Parsed expression should be a MoveExpr")
//        );
//    }
//
//    @Test
//    void testParseIfStatement() throws IOException {
//        Parser parser = createParser("if (0) then done else move downleft");
//        Expr expr = parser.parse();
//
//        assertInstanceOf(IfStatementExpr.class, expr, "Parsed expression should be an IfStatementExpr");
//        //expr.execute(); // ทดสอบการ execute เพื่อให้เกิดผลลัพธ์จริง
//    }
//
//    @Test
//    void testParseWhileStatement() throws IOException {
//        Parser parser = createParser("while (move up) then move down");
//        Expr expr = parser.parse();
//
//        assertInstanceOf(WhileStatementExpr.class, expr, "Parsed expression should be a WhileStatementExpr");
//        //expr.execute();
//    }
//
//    @Test
//    void testTokenStream() throws IOException {
//        Tokenizer tokenizer = new ExprTokenizer("if (0) then done else move downleft");
//
//        assertTrue(tokenizer.hasNextToken(), "backend.parser.Tokenizer should have tokens available");
//    }
//
//    @Test
//    void testUnexpectedToken() {
//        Parser parser = createParser("$!@");
//
//        Exception exception = assertThrows(IOException.class, parser::parse, "Parsing an unexpected token should throw IOException");
//        assertTrue(exception.getMessage().contains("Unexpected token"), "Error message should indicate an unexpected token");
//    }
//
////    @Test
////    void testFullExecution() throws IOException {
////        HexCell hex = new HexCell(1, 1);
////        Minion minion = new HumanMinion("minion", hex);
////
////        Expr expr = createParser("move down").parse();
////        expr.execute();
////
////        HexCell expectedPosition = new HexCell(2, 1); // ตรวจสอบว่ามินเนียนเคลื่อนที่ลงจริง
////        assertEquals(expectedPosition, minion.getPosition(), "backend.minions.Minion should move down correctly");
////    }
//}
