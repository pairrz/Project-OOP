package backend.evaluation;

import backend.game.*;
import backend.minions.*;
import backend.parser.*;

import java.util.Map;

public record NearbyExpr(String direction, Minion minion) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();
        int distance = 1;

        switch (direction) {
            case "up" -> x--;
            case "down" -> x++;
            case "upleft" -> { x--; y--; }
            case "upright" -> { x--; y++; }
            case "downleft" -> y--;
            case "downright" -> y++;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        while (GameBoard.isValidPosition(x, y)) {
            Minion target = GameBoard.getHexCell(x, y).getMinion();
            if (target != null) {
                int result = 100 * target.getHP() + 10 * target.getDef() + distance;
                bindings.put("nearby", result);
                return result;
            }
            x += (direction.contains("up") ? -1 : 0) + (direction.contains("down") ? 1 : 0);
            y += (direction.contains("left") ? -1 : 0) + (direction.contains("right") ? 1 : 0);
            distance++;
        }
        bindings.put("nearby", 0);
        return 0;
    }
}
