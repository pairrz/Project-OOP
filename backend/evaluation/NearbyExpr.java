package backend.evaluation;

import backend.game.*;
import backend.minions.*;
import backend.parser.Expr;

import java.util.Map;

public record NearbyExpr(String direction, Minion minion) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();
        int distance = 1;
        int result = 0; // ✅ เก็บค่าผลลัพธ์

        while (GameBoard.isValidPosition(x, y)) {
            switch (direction) {
                case "up" -> x--;
                case "down" -> x++;
                case "upleft" -> { x--; y--; }
                case "upright" -> { x--; y++; }
                case "downleft" -> { x++; y--; }
                case "downright" -> { x++; y++; }
                default -> throw new IllegalArgumentException("Invalid direction: " + direction);
            }

            if (!GameBoard.isValidPosition(x, y)) break;

            HexCell cell = GameBoard.getHexCell(x, y);
            if (cell == null) continue;

            Minion target = cell.getMinion();
            if (target != null) {
                int hpValue = target.getHP();
                int defValue = target.getDef();
                result = 100 * hpValue + 10 * defValue + distance;

                result = (target.getOwner() == minion.getOwner()) ? -result : result;
                break; // ✅ เจอมินเนียนแล้วออกจากลูป
            }

            distance++;
        }

        bindings.put("x", result); // ✅ บันทึกค่าให้ bindings
        return result;
    }
}