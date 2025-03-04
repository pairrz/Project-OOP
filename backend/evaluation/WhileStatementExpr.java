package backend.evaluation;

import SyntaxErrorException.DoneException;
import backend.parser.Expr;
import java.util.Map;

public record WhileStatementExpr(Expr condition, Expr statement) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        try {
            int result = 0;
            int count = 0;
            while (condition.eval(bindings) > 0 && count <= 10000) {
                System.out.println("While Loop Condition: " + condition.eval(bindings) + " | Count: " + count);
                result = statement.eval(bindings);
                count++;
            }
            System.out.println("Exit While Loop: " + condition.eval(bindings) + " | Count: " + count);
            return result;
        } catch (DoneException e) {
            System.out.println("While loop stopped due to 'done' command.");
            throw e; // ส่ง Exception ออกไปเพื่อให้ Strategy หยุดทันที
        }
    }

}



