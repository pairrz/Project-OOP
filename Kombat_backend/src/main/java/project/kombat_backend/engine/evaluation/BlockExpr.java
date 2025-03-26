package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.exception.DoneException;
import project.kombat_backend.engine.parser.Expr;
import java.util.List;
import java.util.Map;

public record BlockExpr(List<Expr> statements) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        try {
            int result = 0;
            for (Expr statement : statements) {
                result = statement.eval(bindings);
            }
            return result;
        } catch (DoneException e) {
            //System.out.println("Block execution stopped due to 'done' command.");
            throw e;
        }
    }
}