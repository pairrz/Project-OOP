package backend.parser;

import backend.evaluation.*;
import backend.minions.*;
import backend.players.*;

import java.io.IOException;
import java.util.*;

public class ExprParse implements Parser {
    private final List<String> commandWords = List.of("done", "move", "shoot");
    protected Tokenizer token;
    protected Player player;
    protected Map<String, Integer> playerBindings = new HashMap<>();
    protected Minion minion;

    public ExprParse(Tokenizer token, Minion minion) {
        this.token = token;
        this.player = minion.getOwner();
        this.minion = minion;
        playerBindings.put("budget", player.getBudget());
    }

    @Override
    public Expr parse() throws Exception {
        Expr expr = Strategy();
        if (token.hasNextToken()) {
            throw new IOException("Unexpected token remaining: " + token.peek());
        }
        return expr;
    }

    private Expr Strategy() throws IOException {
        List<Expr> statements = new ArrayList<>();
        while (token.hasNextToken()) {  // ✅ อ่านทุกคำสั่งที่เหลือ
            statements.add(Statement());
        }
        return new BlockExpr(statements);
    }

    private Expr Statement() throws IOException {
        if (!token.hasNextToken()) {
            throw new IOException("Unexpected end of input");
        }

        String str = token.peek();
        //System.out.println("Parsing statement, peek: " + str);  // Debugging

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
                token.consume("while");
                token.consume("(");
                Expr expr = Expression();
                token.consume(")");
                Expr statement1 = Statement();
                return new WhileStatementExpr(expr, statement1);
            }
            case "{" -> {
                token.consume("{");
                List<Expr> statements = new ArrayList<>();
                while (!token.peek("}")) {
                    statements.add(Statement());
                }
                token.consume("}");
                return new BlockExpr(statements);
            }
            case null, default -> {
                return Command();
            }
        }
    }

    private Expr Command() throws IOException {
        if (!token.hasNextToken()) {
            throw new IOException("Expected command but found end of input");
        }
        String str = token.peek();
        //System.out.println("Parsing command, peek: " + str);  // Debugging

        if (checkComWord(str)) {
            return ActionCommand();
        } else {
            return AssignmentStatement();
        }
    }

    private Expr AssignmentStatement() throws IOException {
        String var = token.consume();
        if (!token.hasNextToken() || !token.peek().equals("=")) {
            throw new IOException("Expected '=' after variable name but found: " + token.peek());
        }
        token.consume("=");

        if (!token.hasNextToken()) {
            throw new IOException("Unexpected end of input after '='");
        }

        String str = token.peek();
        if (str.equals("nearby") || str.equals("ally") | str.equals("opponent")) {
            return InfoExpression(); // ✅ ให้ InfoExpression() จัดการ nearby
        } else {
            return new AssignmentExpr(var, Expression());
        }
    }

    private Expr ActionCommand() throws IOException {
        if (!token.hasNextToken()) {
            throw new IOException("Expected action command but found end of input");
        }

        String str = token.peek();
        switch (str) {
            case "done" -> {
                token.consume("done");
                return new DoneExpr();
            }
            case "move" -> {
                return MoveCommand();
            }
            case "shoot" -> {
                return AttackCommand();
            }
            default -> throw new IOException("Invalid action command: " + token.peek());
        }
    }

    private Expr MoveCommand() throws IOException {
        token.consume("move");
        return new MoveExpr(minion, Direction());
    }

    private Expr AttackCommand() throws IOException {
        token.consume("shoot");  // ✅ Consume "shoot" ก่อน
        String direction = Direction(); // ✅ อ่าน Direction อย่างถูกต้อง
        Expr expr = Expression(); // ✅ จากนั้นอ่านค่าความแรงของการโจมตี
        return new AttackExpr(minion, direction, expr);
    }

    private String Direction() throws IOException {
        String dir = token.peek();
        if (!List.of("up", "down", "upleft", "upright", "downleft", "downright").contains(dir)) {
            throw new IOException("Invalid direction: " + dir);
        }

        return token.consume();
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
        if (Character.isDigit(token.peek().charAt(0))) {
            return new IntLit(Integer.parseInt(token.consume()));
        } else if (Character.isLetter(token.peek().charAt(0))) {
            return new Variable(token.consume(), minion);
        } else if (token.peek("(")) {
            token.consume("(");
            Expr expr = Expression();
            if (!token.peek(")")) {
                throw new IOException("Expected ')' but found: " + token.peek());
            }
            token.consume(")");
            return expr;
        }

        // ✅ ให้ InfoExpression() รองรับ nearby + ตัวดำเนินการทางคณิตศาสตร์
        return InfoExpression();
    }


    private Expr InfoExpression() throws IOException {
        if (token.peek("ally") || token.peek("opponent")) {
            String type = token.consume();
            return new InfoExpr(type, minion);
        } else if (token.peek("nearby")) {
            token.consume("nearby");
            if (!token.hasNextToken()) {
                throw new IOException("Unexpected end of input: Expected direction after 'nearby'");
            }

            String direction = token.consume();
            if (!List.of("up", "down", "upleft", "upright", "downleft", "downright").contains(direction)) {
                throw new IOException("Invalid direction for 'nearby': " + direction);
            }

            // ✅ สร้าง nearbyExpr ก่อน
            Expr nearbyExpr = new NearbyExpr(direction, minion);

            // ✅ ตรวจสอบว่ามีการดำเนินการทางคณิตศาสตร์หลัง nearby หรือไม่
            if (token.peek("*") || token.peek("/") || token.peek("%") || token.peek("+") || token.peek("-") || token.peek("^")) {
                return new BinaryArithExpr(nearbyExpr, token.consume(), Expression());
            }

            return nearbyExpr;
        } else {
            throw new IOException("Unexpected info expression: " + token.peek());
        }
    }


    private boolean checkComWord(String word) {
        return commandWords.contains(word);
    }
}