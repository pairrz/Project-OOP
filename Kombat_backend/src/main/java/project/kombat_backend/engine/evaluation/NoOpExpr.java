package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.parser.Expr;
import java.util.Map;

public record NoOpExpr() implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) {
        return 0;
    }
}