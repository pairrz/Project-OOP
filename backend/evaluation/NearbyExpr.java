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
                int hpValue = target.getHP();
                int defValue = target.getDef();
                int result = 100 * hpValue + 10 * defValue + distance;

                return (target.getOwner() == minion.getOwner()) ? -result : result;
            }

            switch (direction) {
                case "up" -> x--;
                case "down" -> x++;
                case "upleft" -> { x--; y--; }
                case "upright" -> { x--; y++; }
                case "downleft" -> y--;
                case "downright" -> y++;
            }
            distance++;
        }

        return 0;
    }
}
