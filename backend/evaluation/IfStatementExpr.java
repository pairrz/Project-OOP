package backend.evaluation;

import backend.parser.Expr;

import java.util.Map;

public record IfStatementExpr(Expr condition, Expr thenExpr, Expr elseExpr) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int condValue = condition.eval(bindings);

        if (condValue > 0) {
            //System.out.println("Executing THEN branch");
            return thenExpr.eval(bindings);
        } else {
            //System.out.println("Executing ELSE branch");
            return elseExpr.eval(bindings);
        }
    }
}

