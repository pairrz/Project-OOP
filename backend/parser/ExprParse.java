package backend.parser;

import backend.evaluation.*;
import backend.minions.*;
import backend.players.*;
import backend.game.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExprParse implements Parser {
    private final List<String> commandWords = List.of("done", "move", "shoot");
    protected Tokenizer token;
    protected Player player;
    protected Map<String, Integer> playerBindings = new HashMap<>();
    protected Minion minion;

    public ExprParse(Tokenizer token, Minion minion) throws IOException {
        this.token = token;
        this.player = GameBoard.getInstance(GameBoard.namePlayerOne,GameBoard.namePlayerTwo).getCurrentPlayer();
        this.minion = minion;
        GameBoard board = GameBoard.getInstance(GameBoard.namePlayerOne, GameBoard.namePlayerTwo);
        playerBindings.put("budget", player.getBudget());
    }

    @Override
    public Expr parse() throws IOException {
        Expr expr = Strategy();
        if (token.hasNextToken()) {
            throw new IOException("Leftover token on line ");
        }
        return expr;
    }

    private Expr Strategy() throws IOException {
        Expr expr = null;

        while (token.hasNextToken()) {  // Loop ไปจนกว่าหมด token
            Expr nextExpr = Statement();

            if (expr == null) {
                expr = nextExpr;  // ถ้าเป็น statement แรก ให้กำหนด expr เป็นค่าแรก
            } else {
                Expr finalExpr = expr;
                expr = new Expr() {  // ใช้ Anonymous Class เพื่อรวม Statement
                    @Override
                    public int eval(Map<String, Integer> bindings) throws Exception {
                        finalExpr.eval(bindings);  // ประมวลผล statement ก่อนหน้า
                        return nextExpr.eval(bindings);  // ประมวลผล statement ปัจจุบัน
                    }
                };
            }
        }
        return expr;
    }

    private Expr Statement() throws IOException {
        //System.out.println("in statement");

        if (!token.hasNextToken()) {
            throw new IOException("Unexpected end of statement");
        }

        if (token.peek("if")) {
            token.consume("if");
            token.consume("(");
            Expr condition = Expression();
            token.consume(")");

            token.consume("then");

            Expr statement1;
            if (token.peek("{")) {
                statement1 = BlockStatement();
            } else {
                statement1 = Statement();
            }

            token.consume("else");

            Expr statement2;
            if (token.peek("{")) {
                statement2 = BlockStatement();
            } else {
                statement2 = Statement();
            }

            return new IfStatementExpr(condition, statement1, statement2);
        } else if (token.peek("while")) {
            token.consume("while");
            token.consume("(");
            Expr condition = Expression();
            token.consume(")");

            if (!token.peek("{")) {
                throw new IOException("Expected '{' after while condition");
            }

            return new WhileStatementExpr(condition, BlockStatement());

        } else if (token.peek("{")) {
            return BlockStatement();

        } else {
            return Command();
        }
    }


    private Expr BlockStatement() throws IOException {
        token.consume("{");

        List<Expr> statements = new ArrayList<>();
        while (token.hasNextToken() && !token.peek("}")) { // อ่านจนกว่าจะเจอ }
            statements.add(Statement());
        }

        if (!token.hasNextToken()) {
            throw new IOException("Unexpected end of block, missing '}'");
        }

        token.consume("}");

        return statements.isEmpty() ? new NoOpExpr() : new BlockExpr(statements);
    }

    private Expr Command() throws IOException {
        //System.out.println("in command");
        String str = token.peek();
        if (checkComWord(str)) {
            return ActionCommand();
        } else {
            return AssignmentStatement();
        }
    }

    private Expr AssignmentStatement() throws IOException {
        //System.out.println("in assignment");
        String var = token.consume();

        if (!token.peek("=")) {
            throw new IOException("= expected after variable: " + var);
        }

        token.consume("=");
        Expr expr = Expression();  // คำนวณค่าที่กำหนดให้ตัวแปร
        return new AssignmentExpr(var, expr);
    }


    private Expr ActionCommand() throws IOException {
        //System.out.println("in action");
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
        //System.out.println("in move");
        token.consume("move");
        return new MoveExpr(minion, Direction());
    }

    private Expr AttackCommand() throws IOException {
        //System.out.println("in attack");
        token.consume("shoot");
        String direction = Direction();
        Expr expend = Expression();
        return new AttackExpr(minion, direction, expend);
    }

    private String Direction() throws IOException {
        //System.out.println("in direction");
        if (token.peek("up") || token.peek("down") || token.peek("upleft") || token.peek("upright") ||
                token.peek("downleft") || token.peek("downright")) {
            return token.consume();
        } else {
            throw new IOException("Expected direction but found: " + token.peek());  // Changed to IOException
        }
    }

    private Expr Expression() throws IOException {
        //System.out.println("in expression");
        Expr expr = Term();
        while (token.peek("+") || token.peek("-")) {
            String op = token.consume();
            expr = new BinaryArithExpr(expr, op, Term());
        }
        return expr;
    }

    private Expr Term() throws IOException {
        //System.out.println("in term");
        Expr expr = Factor();
        while (token.peek("*") || token.peek("/") || token.peek("%")) {
            String op = token.consume();
            expr = new BinaryArithExpr(expr, op, Factor());
        }
        return expr;
    }

    private Expr Factor() throws IOException {
        //System.out.println("in factor");
        Expr expr = Power();
        while (token.peek("^")) {
            token.consume("^");
            expr = new BinaryArithExpr(expr, "^", Factor());
        }
        return expr;
    }

    private Expr Power() throws IOException {
        //System.out.println("in power");

        if (token.peek().matches("\\d+")) {
            return new IntLit(Integer.parseInt(token.consume()));
        }else if (token.peek("nearby") || token.peek("ally") || token.peek("opponent")) {
            return InfoExpression();
        }else if (Character.isLetter(token.peek().charAt(0))) {
            return new Variable(token.consume(), minion);
        }else if (token.peek("(")) {
            token.consume("(");
            Expr expr = Expression();
            token.consume(")");
            return expr;
        }
        throw new IOException("Unexpected token in Power(): " + token.peek());
    }

    private Expr InfoExpression() throws IOException {
        //System.out.println("in info expression: ");

        if (token.peek("ally") || token.peek("opponent")) {
            return new InfoExpr(token.consume(), minion);
        } else if (token.peek("nearby")) {
            token.consume("nearby");
            String direction = Direction();  // ตรวจสอบว่า direction ถูกรับมาอย่างถูกต้อง
            return new NearbyExpr(direction, minion);
        } else {
            throw new IOException("Unexpected info expression: " + token.peek());
        }
    }


    private boolean checkComWord(String word) {
        return commandWords.contains(word);
    }
}



