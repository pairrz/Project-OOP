import SyntaxErrorException.SyntaxError;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Character.isDigit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Character.isDigit;

public class ExprParse implements Parser {
    private final List<String> resWords = List.of("ally", "done", "down", "downleft", "downright", "else", "if", "move", "nearby", "opponent", "shoot", "then", "up", "upleft", "upright", "while");
    private final List<String> commandWords = List.of("done", "move", "shoot");
    protected Tokenizer token;
    private final GameBoard board = GameBoard.getInstance();
    protected Player player;
    protected Map<String, Integer> playerBindings = new HashMap<>();
    protected Minion minion;

    public ExprParse(Tokenizer token, Player player, Minion minion) {
        this.token = token;
        this.player = player;
        this.minion = minion;
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
        if (token.hasNextToken()) {
            expr = Statement();
        }
        return expr;
    }

    private Expr Statement() throws IOException {
        if (token.peek("if")) {
            token.consume("if");
            token.consume("(");  // Consume opening parenthesis
            Expr expr = Expression();
            token.consume(")");  // Consume closing parenthesis
            token.consume("then");
            Expr statement1 = Statement();
            token.consume("else");
            Expr statement2 = Statement();
            return new IfStatementExpr(expr, statement1, statement2);
        } else if (token.peek("while")) {
            token.consume("while");
            token.consume("(");  // Consume opening parenthesis
            Expr expr = Expression();
            token.consume(")");  // Consume closing parenthesis
            Expr statement1 = Statement();
            return new WhileStatementExpr(expr, statement1);
        } else if (token.peek("{")) {
            token.consume("{");
            Expr expr = Statement();
            token.consume("}");
            return expr;
        } else {
            return Command();
        }
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
        return new MoveExpr(minion, Direction(), board);
    }

    private Expr AttackCommand() throws IOException {
        token.consume("shoot");
        Expr expend = Expression();
        return new AttackExpr(player, minion, Direction(), expend, board);
    }

    private String Direction() throws IOException {
        if (token.peek("up") || token.peek("down") || token.peek("upleft") || token.peek("upright") ||
                token.peek("downleft") || token.peek("downright")) {
            String direction = token.consume();
            return direction;
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
        // Check if it's a number
        try {
            if (isDigit(Integer.parseInt(token.peek()))) {
                return new IntLit(Integer.parseInt(token.consume()));  // Convert to IntLit if it's a number
            }
        } catch (NumberFormatException e) {
            // If it's not a number
            // Check if it's a variable (letter)
            if (Character.isLetter(token.peek().charAt(0))) {
                return new Variable(token.consume(), player, minion, board);  // Use as variable
            } else if (token.peek("(")) {
                token.consume("(");
                Expr expr = Expression();
                token.consume(")");
                return expr;
            }
        }

        // Look for non-number evaluation value
        return InfoExpression();
    }

    private Expr InfoExpression() throws IOException {
        if (token.peek("ally") || token.peek("opponent")) {
            return new InfoExpr(token.consume(), player, minion, board);
        } else if (token.peek("nearby")) {
            token.consume("nearby");
            return new NearbyExpr(Direction(), player, minion, board);
        } else {
            throw new IOException("Unexpected info expression: " + token.peek());  // Changed to IOException
        }
    }

    private boolean checkComWord(String word) {
        return commandWords.contains(word);
    }
}



