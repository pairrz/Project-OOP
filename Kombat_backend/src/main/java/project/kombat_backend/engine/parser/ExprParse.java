package project.kombat_backend.engine.parser;

import project.kombat_backend.engine.exception.DoneException;
import project.kombat_backend.engine.evaluation.*;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.minion.Minion;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.service.GameManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExprParse implements Parser {
    private final List<String> commandWords = List.of("done", "move", "shoot");
    protected Tokenizer token;
    protected Player player;
    protected GameBoard gameBoard;
    protected GameManager gameManager;
    protected Map<String, Integer> playerBindings = new HashMap<>();
    protected Minion minion;

    public ExprParse(Tokenizer token, Minion minion, GameBoard gameBoard,GameManager gameManager) {
        this.token = token;
        this.player = minion.getOwner();
        this.minion = minion;
        this.gameBoard = gameBoard;
        this.gameManager = gameManager;
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
        while (token.hasNextToken()) {
            Expr nextExpr = Statement();
            if (expr == null) {
                expr = nextExpr;
            } else {
                Expr finalExpr = expr;
                expr = bindings -> {
                    try {
                        finalExpr.eval(bindings);
                        return nextExpr.eval(bindings);
                    } catch (DoneException e) {
                        throw e;
                    }
                };
            }
        }
        return expr;
    }

    private Expr Statement() throws IOException {
        if (!token.hasNextToken()) {
            throw new IOException("Unexpected end of statement");
        }

        if (token.peek("if")) {
            token.consume("if");
            token.consume("(");
            Expr condition = Expression();
            token.consume(")");

            token.consume("then");
            Expr statement1 = token.peek("{") ? BlockStatement() : Statement();

            Expr statement2 = new NoOpExpr();
            if (token.peek("else")) {
                token.consume("else");
                statement2 = token.peek("{") ? BlockStatement() : Statement();
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
        while (token.hasNextToken() && !token.peek("}")) {
            statements.add(Statement());
        }

        if (!token.hasNextToken()) {
            throw new IOException("Unexpected end of block, missing '}'");
        }

        token.consume("}");

        return statements.isEmpty() ? new NoOpExpr() : new BlockExpr(statements);
    }

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

        if (!token.peek("=")) {
            throw new IOException("= expected after variable: " + var);
        }

        token.consume("=");
        Expr expr = Expression();  // คำนวณค่าที่กำหนดให้ตัวแปร
        return new AssignmentExpr(var, expr);
    }


    private Expr ActionCommand() throws IOException {
        if (token.peek("done")) {
            token.consume("done");
            return new DoneExpr();
        } else if (token.peek("move")) {
            return MoveCommand();
        } else if (token.peek("shoot")) {
            return AttackCommand();
        } else {
            throw new IOException("Invalid action command: " + token.peek());  // Changed to IOException
        }
    }

    private Expr MoveCommand() throws IOException {
        token.consume("move");
        return new MoveExpr(minion, Direction(),gameBoard);
    }

    private Expr AttackCommand() throws IOException {
        token.consume("shoot");
        String direction = Direction();
        Expr expend = Expression();
        return new AttackExpr(minion, direction, expend,gameBoard);
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
        if (token.peek().matches("\\d+")) {
            return new IntLit(Integer.parseInt(token.consume()));
        }else if (token.peek("nearby") || token.peek("ally") || token.peek("opponent")) {
            return InfoExpression();
        }else if (Character.isLetter(token.peek().charAt(0))) {
            return new Variable(token.consume(), minion,gameBoard,gameManager);
        }else if (token.peek("(")) {
            token.consume("(");
            Expr expr = Expression();
            token.consume(")");
            return expr;
        }
        throw new IOException("Unexpected token in Power(): " + token.peek());
    }

    private Expr InfoExpression() throws IOException {
        if (token.peek("ally") || token.peek("opponent")) {
            return new InfoExpr(token.consume(), minion,gameBoard);
        } else if (token.peek("nearby")) {
            token.consume("nearby");
            String direction = Direction();  // ตรวจสอบว่า direction ถูกรับมาอย่างถูกต้อง
            return new NearbyExpr(direction, minion, gameBoard);
        } else {
            throw new IOException("Unexpected info expression: " + token.peek());
        }
    }

    private boolean checkComWord(String word) {
        return commandWords.contains(word);
    }
}