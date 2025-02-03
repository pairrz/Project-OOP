import java.util.Map;

public class IfStatementExpr implements Expr {
    Expr condition;
    Expr thenExpr;
    Expr elseExpr;

    public IfStatementExpr(Expr condition, Expr thenExpr, Expr elseExpr) {
        this.condition = condition;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        if (condition.eval(bindings) > 0) {
            return thenExpr.eval(bindings);
        } else {
            return elseExpr.eval(bindings);
        }
    }

}
