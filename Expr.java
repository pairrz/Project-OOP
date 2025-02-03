import SyntaxErrorException.SyntaxError;
import java.util.List;
import java.util.Map;
import static java.lang.Character.isDigit;

public interface Expr{
    int eval(Map<String, Integer> bindings) throws Exception;
}

class Parse implements Parser{
    private final List<String> resWords = List.of("ally", "done", "down", "downleft", "downright", "else", "if", "move", "nearby", "opponent", "shoot", "then", "up", "upleft", "upright", "while");
    private final List<String> commandWords = List.of("done","move","shoot");
    private Tokenizer token;
    private Minion minion;
    private GameBoard board;

    public Parse(Tokenizer token, Minion minion) {
        this.token = token;
        this. minion= minion;
    }

    @Override
    public Expr parse() throws SyntaxError {
        return Statement();
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
        return new MoveExpr(minion, Direction(),);
    }

    private Expr AttackCommand() {
        token.consume("shoot");
        Expr expenditure = Expression(); // ค่าโจมตีที่มินเนียนจะใช้
        return new AttackExpr(minion, Direction(), expenditure, board);
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
            return new Variable(token.consume());
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
            return new InfoExpr(token.consume());
        } else if (token.peek("nearby")) {
            token.consume("nearby");
            return new NearbyExpr(Direction());
        } else {
            throw new SyntaxError("Unexpected info expression: " + token.peek());
        }
    }

    private boolean checkResWord(String word) {
        return resWords.contains(word);
    }

    private boolean checkComWord(String word) {
        return commandWords.contains(word);
    }
}
