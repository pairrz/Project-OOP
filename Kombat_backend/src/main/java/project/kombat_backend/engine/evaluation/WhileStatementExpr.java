package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.exception.DoneException;
import project.kombat_backend.engine.parser.Expr;

import java.util.Map;

public record WhileStatementExpr(Expr condition, Expr statement) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        try {
            int result = 0;
            int count = 0;
            while (condition.eval(bindings) > 0 && count <= 10000) {
                result = statement.eval(bindings);
                count++;
            }
            return result;
        } catch (DoneException e) {
            //System.out.println("While loop stopped due to 'done' command.");
            throw e;
        }
    }

}



