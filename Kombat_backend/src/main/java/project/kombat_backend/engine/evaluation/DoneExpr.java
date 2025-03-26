package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.exception.DoneException;
import project.kombat_backend.engine.parser.Expr;
import java.util.Map;

public record DoneExpr() implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        //System.out.println("Executing DoneExpr - Ending strategy evaluation for this turn.");
        throw new DoneException();
    }
}
