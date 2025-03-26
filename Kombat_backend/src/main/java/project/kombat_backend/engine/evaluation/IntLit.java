package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.parser.Expr;
import java.util.Map;

public record IntLit(int val) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        return val;
    }
}
