package backend.evaluation;

import backend.parser.Expr;

import java.util.Map;

public record WhileStatementExpr(Expr condition, Expr statement) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int result = 0;
        int count = 0;
        while (condition.eval(bindings) > 0 && count <= 10000){
            result = statement.eval(bindings);
            count++;
        }
        //System.out.println("while");
        return result;
    }
}
