import java.util.Map;

public class AssignmentExpr implements Expr {
    String var;
    Expr expr;
    public AssignmentExpr(String var, Expr expr) {
        this.var = var;
        this.expr = expr;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int result = expr.eval(bindings);
        bindings.put(var, result);
        return result;
    }

}
