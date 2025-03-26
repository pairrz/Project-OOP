package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.parser.Expr;
import java.util.Map;

public record BinaryArithExpr(Expr left, String op, Expr right) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int lv = left.eval(bindings);
        int rv = right.eval(bindings);

        if (("/".equals(op) || "%".equals(op)) && rv == 0) {
            throw new ArithmeticException("Division by zero");
        }

        return switch (op) {
            case "+" -> lv + rv;
            case "-" -> lv - rv;
            case "*" -> lv * rv;
            case "/" -> lv / rv;
            case "%" -> lv % rv;
            case "^" -> (int) Math.pow(lv, rv);
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }
} 
