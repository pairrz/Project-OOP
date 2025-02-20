package backend.evaluation;

import backend.parser.Expr;
import java.util.Map;

public record WhileStatementExpr(Expr condition, Expr statement) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        System.out.println("Evaluating While loop...");
        System.out.println("Condition: " + condition.eval(bindings));  // พิมพ์ผลการประมวลผลของเงื่อนไข
        System.out.println("Statement: " + statement.eval(bindings));  // พิมพ์ผลการประมวลผลของคำสั่ง
        if (condition == null || statement == null) {
            throw new IllegalArgumentException("เงื่อนไขหรือคำสั่งว่างเปล่า!");
        }

        int counter = 0;
        while (condition.eval(bindings) > 0) {
            System.out.println("Entering while loop...");
            if (counter > 10000) {
                System.out.println("While loop เกินขีดจำกัด!");
                break;
            }
            statement.eval(bindings);  // อัพเดต bindings ด้วยค่าใหม่ของ x
            counter++;
        }
        return 0;
    }
}



