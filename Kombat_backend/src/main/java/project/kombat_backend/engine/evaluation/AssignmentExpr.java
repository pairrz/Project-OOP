package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.parser.Expr;
import java.util.Map;

public record AssignmentExpr(String var, Expr expr) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception{
        int result = expr.eval(bindings);
        bindings.put(var, result);
        return result;
    }
}
