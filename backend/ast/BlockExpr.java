package backend.ast;

import SyntaxErrorException.DoneException;
import backend.parser.Expr;
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
            throw e;
        }
    }
}