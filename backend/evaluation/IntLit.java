package backend.evaluation;

import backend.parser.Expr;

import java.util.Map;

public record IntLit(int val) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        return val;
    }
}
