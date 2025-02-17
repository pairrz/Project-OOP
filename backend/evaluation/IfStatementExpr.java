package backend.evaluation;

import backend.parser.Expr;

import java.util.Map;

public record IfStatementExpr(Expr condition, Expr thenExpr, Expr elseExpr) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        if (condition.eval(bindings) > 0) {
            return thenExpr.eval(bindings);
        } else {
            return elseExpr.eval(bindings);
        }
    }
}
