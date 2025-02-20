package backend.parser;

import backend.evaluation.*;
import backend.minions.*;
import backend.players.*;
import backend.game.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExprParse implements Parser {
    private final List<String> commandWords = List.of("done", "move", "shoot");
    protected Tokenizer token;
    private final GameBoard board;
    protected Player player;
    protected Map<String, Integer> playerBindings = new HashMap<>();
    protected Minion minion;

    public ExprParse(Tokenizer token, Minion minion) {
        this.token = token;
        this.player = GameBoard.getInstance(GameBoard.namePlayerOne,GameBoard.namePlayerTwo).getCurrentPlayer();
        this.minion = minion;
        this.board = GameBoard.getInstance(GameBoard.namePlayerOne, GameBoard.namePlayerTwo);
        playerBindings.put("budget", player.getBudget());
    }

    @Override
    public Expr parse() throws Exception {
        return Strategy();
    }

    private Expr Strategy() throws IOException {
        return Statement();
    }

    private Expr Statement() throws IOException {
        String str = token.peek();
        System.out.println("peek: " + str);  // เพิ่มการพิมพ์ดีบัก
        switch (str) {
            case "if" -> {
                token.consume("if");
                token.consume("(");
                Expr expr = Expression();
                token.consume(")");
                token.consume("then");
                Expr statement1 = Statement();
                token.consume("else");
                Expr statement2 = Statement();
                return new IfStatementExpr(expr, statement1, statement2);
            }
            case "while" -> {
                System.out.println("While detected");  // เพิ่มการพิมพ์ดีบัก

                token.consume("while");
                token.consume("(");  // Consume opening parenthesis

                Expr expr = Expression();
                token.consume(")");  // Consume closing parenthesis

                Expr statement1 = Statement();
                return new WhileStatementExpr(expr, statement1);
            }
            case "{" -> {
                token.consume("{");
                Expr expr = Statement();
                token.consume("}");
                return expr;
            }
            default -> {
                return Command();
            }
        }
    }

//    private Expr Statement() throws IOException {
//        if (token.peek("if")) {
//            token.consume("if");
//            token.consume("(");  // Consume opening parenthesis
//            Expr expr = Expression();
//            token.consume(")");  // Consume closing parenthesis
//            token.consume("then");
//            Expr statement1 = Statement();
//            token.consume("else");
//            Expr statement2 = Statement();
//            return new IfStatementExpr(expr, statement1, statement2);
//        } else if (token.peek("while")) {
//            token.consume("while");
//            token.consume("(");  // Consume opening parenthesis
//            Expr expr = Expression();
//            token.consume(")");  // Consume closing parenthesis
//            Expr statement1 = Statement();
//            return new WhileStatementExpr(expr, statement1);
//        } else if (token.peek("{")) {
//            token.consume("{");
//            Expr expr = Statement();
//            token.consume("}");
//            return expr;
//        } else {
//            return Command();
//        }
//    }

    private Expr Command() throws IOException {
        String str = token.peek();
        if (checkComWord(str)) {
            return ActionCommand();
        } else {
            return AssignmentStatement();
        }
    }

    private Expr AssignmentStatement() throws IOException {
        String var = token.consume();
        token.consume("=");
        Expr expr = Expression();
        return new AssignmentExpr(var, expr);
    }

    private Expr ActionCommand() throws IOException {
        if (token.peek("done")) {
            token.consume("done");
            return new DoneExpr();
        } else if (token.peek("move")) {
            return MoveCommand();  // MoveCommand() ถูกต้อง
        } else if (token.peek("shoot")) {
            return AttackCommand();
        } else {
            throw new IOException("Invalid action command: " + token.peek());  // Changed to IOException
        }
    }

    private Expr MoveCommand() throws IOException {
        token.consume("move");
        return new MoveExpr(minion, Direction());
    }

    private Expr AttackCommand() throws IOException {
        token.consume("shoot");
        Expr expr = Expression();
        return new AttackExpr(minion, Direction(), expr);
    }

    private String Direction() throws IOException {
        if (token.peek("up") || token.peek("down") || token.peek("upleft") || token.peek("upright") ||
                token.peek("downleft") || token.peek("downright")) {
            return token.consume();
        } else {
            throw new IOException("Expected direction but found: " + token.peek());  // Changed to IOException
        }
    }

    private Expr Expression() throws IOException {
        Expr expr = Term();
        while (token.peek("+") || token.peek("-")) {
            String op = token.consume();
            expr = new BinaryArithExpr(expr, op, Term());
        }
        return expr;
    }

    private Expr Term() throws IOException {
        Expr expr = Factor();
        while (token.peek("*") || token.peek("/") || token.peek("%")) {
            String op = token.consume();
            expr = new BinaryArithExpr(expr, op, Factor());
        }
        return expr;
    }

    private Expr Factor() throws IOException {
        Expr expr = Power();
        while (token.peek("^")) {
            token.consume("^");
            expr = new BinaryArithExpr(expr, "^", Factor());
        }
        return expr;
    }

    private Expr Power() throws IOException {
        if (Character.isDigit(token.peek().charAt(0))) {  // ตรวจสอบก่อน parse
            return new IntLit(Integer.parseInt(token.consume()));
        } else if (Character.isLetter(token.peek().charAt(0))) {
            return new Variable(token.consume(), minion);
        } else if (token.peek("(")) {
            token.consume("(");
            Expr expr = Expression();
            token.consume(")");
            return expr;
        }
        return InfoExpression();
    }


    private Expr InfoExpression() throws IOException {
        if (token.peek("ally") || token.peek("opponent")) {
            return new InfoExpr(token.consume(), minion);
        } else if (token.peek("nearby")) {
            token.consume("nearby");
            return new NearbyExpr(Direction(), minion);
        } else {
            throw new IOException("Unexpected info expression: " + token.peek());  // Changed to IOException
        }
    }

    private boolean checkComWord(String word) {
        return commandWords.contains(word);
    }
}



