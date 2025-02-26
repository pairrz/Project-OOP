package backend.evaluation;

import backend.parser.Expr;

import java.util.List;
import java.util.Map;

public record BlockExpr(List<Expr> statements) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int result = 0;
        for (Expr statement : statements) {
            result = statement.eval(bindings);
        }
        return result;
    }
}
