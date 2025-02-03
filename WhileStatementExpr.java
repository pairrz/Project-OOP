import java.util.Map;

public class WhileStatementExpr implements Expr {
    Expr condition;
    Expr statement;

    public WhileStatementExpr(Expr condition, Expr statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int result = 0;
        while (condition.eval(bindings) > 0){
            result = statement.eval(bindings);
        }
        return 0;
    }
}
