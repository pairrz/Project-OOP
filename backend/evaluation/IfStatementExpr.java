package backend.evaluation;

import backend.parser.Expr;

import java.util.Map;

public record IfStatementExpr(Expr condition, Expr thenExpr, Expr elseExpr) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        // ใช้ค่าของ condition แทน < หรือ > แล้วตรวจสอบว่ามากกว่า 0 หรือไม่
        return (condition.eval(bindings) > 0) ? thenExpr.eval(bindings) : elseExpr.eval(bindings);
    }
}

