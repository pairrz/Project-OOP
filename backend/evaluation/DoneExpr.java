package backend.evaluation;

import SyntaxErrorException.DoneException;
import backend.parser.Expr;

import java.util.Map;

public record DoneExpr() implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        System.out.println("Executing DoneExpr - Ending strategy evaluation for this turn.");
        throw new DoneException();  // ใช้ Exception เพื่อหยุดการทำงานทั้งหมด
    }
}
