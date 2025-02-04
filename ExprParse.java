import SyntaxErrorException.SyntaxError;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Character.isDigit;

public class ExprParse implements Parser{
    private final List<String> resWords = List.of("ally", "done", "down", "downleft", "downright", "else", "if", "move", "nearby", "opponent", "shoot", "then", "up", "upleft", "upright", "while");
    private final List<String> commandWords = List.of("done","move","shoot");
    protected Tokenizer token;
    private final GameBoard board = GameBoard.getInstance();
    protected Player player;
    protected Map<String, Integer> playerBindings = new HashMap<>();
    protected Minion minion;

    public ExprParse(Tokenizer token,Player player, Minion minion) {
        this.token = token;
        this.player = player;
        this. minion= minion;
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

    private Expr Statement() {
        if(token.peek("if")){
            token.consume("if");
            token.consume("(");
            Expr expr = Expression();
            token.consume(")");
            token.consume("then");
            Expr statement1 = Statement();
            token.consume("else");
            Expr statement2 = Statement();
            return new IfStatementExpr(expr,statement1,statement2);
        }else if(token.peek("while")){
            token.consume("while");
            token.consume("(");
            Expr expr = Expression();
            token.consume(")");
            Expr statement1 = Statement();
            return new WhileStatementExpr(expr,statement1);
        }else if(token.peek("{")){
            token.consume("{");
            Expr expr = Statement();
            token.consume("}");
            return expr;
        }else{
            return Command();
        }
    }

    private Expr Command() {
        String str = token.peek();
        if (checkComWord(str)) {
            return ActionCommand();
        } else {
            return AssignmentStatement();
        }
    }

    private Expr AssignmentStatement() {
        String var = token.consume();
        token.consume("=");
        Expr expr = Expression();
        return new AssignmentExpr(var, expr);
    }

    private Expr ActionCommand() {
        if (token.peek("done")) {
            token.consume("done");
            return new DoneExpr();
        } else if (token.peek("move")) {
            return MoveCommand();
        } else if (token.peek("shoot")) {
            return AttackCommand();
        } else {
            throw new SyntaxError("Invalid action command: " + token.peek());
        }
    }

    private Expr MoveCommand() {
        token.consume("move");
        return new MoveExpr(minion, Direction(),board);
    }

    private Expr AttackCommand() {
        token.consume("shoot");
        Expr expend = Expression();
        return new AttackExpr(player,minion, Direction(), expend, board);
    }

    private String Direction() {
        if (token.peek("up") || token.peek("down") || token.peek("upleft") || token.peek("upright") ||
                token.peek("downleft") || token.peek("downright")) {
            return token.consume();
        } else {
            throw new SyntaxError("Expected direction but found: " + token.peek());
        }
    }

    private Expr Expression() {
        Expr expr = Term();
        while (token.peek("+") || token.peek("-")) {
            String op = token.consume();
            expr = new BinaryArithExpr(expr, op, Term());
        }
        return expr;
    }

    private Expr Term() {
        Expr expr = Factor();
        while (token.peek("*") || token.peek("/") || token.peek("%")) {
            String op = token.consume();
            expr = new BinaryArithExpr(expr, op, Factor());
        }
        return expr;
    }

    private Expr Factor() {
        Expr expr = Power();
        while (token.peek("^")) {
            token.consume("^");
            expr = new BinaryArithExpr(expr, "^", Factor());
        }
        return expr;
    }

    private Expr Power() {
        if (isDigit(Integer.parseInt(token.peek()))) {
            return new IntLit(Integer.parseInt(token.consume()));
        } else if (Character.isLetter(token.peek().charAt(0))) {
            return new Variable(token.consume(),player,minion,board);
        } else if (token.peek("(")) {
            token.consume("(");
            Expr expr = Expression();
            token.consume(")");
            return expr;
        } else {
            return InfoExpression();
        }
    }

    private Expr InfoExpression() {
        if (token.peek("ally") || token.peek("opponent")) {
            return new InfoExpr(token.consume() , player ,minion,board);
        } else if (token.peek("nearby")) {
            token.consume("nearby");
            return new NearbyExpr(Direction(),player,minion,board);
        } else {
            throw new SyntaxError("Unexpected info expression: " + token.peek());
        }
    }

//    private boolean checkResWord(String word) {
//        return resWords.contains(word);
//    }

    private boolean checkComWord(String word) {
        return commandWords.contains(word);
    }
}
