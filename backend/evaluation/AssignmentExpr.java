package backend.evaluation;

import backend.parser.Expr;

import java.util.Map;

public record AssignmentExpr(String var, Expr expr) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int result = expr.eval(bindings);
        bindings.put(var, result); // ตรวจสอบว่า bindings อัปเดตจริง
        System.out.println("Updated " + var + " = " + result); // Debugging
        return result;
    }
}
