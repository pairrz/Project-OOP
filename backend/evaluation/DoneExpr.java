package backend.evaluation;

import SyntaxErrorException.DoneException;
import backend.parser.Expr;

import java.util.Map;

public record DoneExpr() implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        throw new DoneException();
    }
}
