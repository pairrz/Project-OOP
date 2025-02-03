import SyntaxErrorException.EvalError;
import java.util.Map;

record IntLit(int val) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) {
        return val;
    }
}

record Variable(String name) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) {
        if (bindings.containsKey(name)) {
            return bindings.get(name);
        }
        throw new EvalError("Undefined variable: " + name);
    }
}

record BinaryArithExpr(Expr left, String op, Expr right) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int lv = left.eval(bindings);
        int rv = right.eval(bindings);

        if ("/".equals(op) || "%".equals(op)) {
            if (rv == 0) throw new EvalError("Division by zero");
        }

        return switch (op) {
            case "+" -> lv + rv;
            case "-" -> lv - rv;
            case "*" -> lv * rv;
            case "/" -> lv / rv;
            case "%" -> lv % rv;
            default -> throw new EvalError("Unknown operator: " + op);
        };
    }
}