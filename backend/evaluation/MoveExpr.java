package backend.evaluation;

import backend.game.*;
import backend.minions.*;
import backend.parser.*;
import backend.players.*;
import java.io.IOException;
import java.util.Map;

public record MoveExpr(Minion minion, String direction) implements Expr {

    public boolean moveDirect() throws IOException {
        try {
            int newX = minion.getX(), newY = minion.getY();

            switch (direction) {
                case "up": newX--; break;
                case "down": newX++; break;
                case "upleft": newX--; newY--; break;
                case "upright": newX--; newY++; break;
                case "downleft": newY--; break;
                case "downright": newY++; break;
                default:
                    throw new IllegalArgumentException("Invalid direction: " + direction);
            }

            if (!GameBoard.isValidPosition(newX, newY)) {
                return false;
            }

            HexCell newCell = GameBoard.getHexCell(newX, newY);
            if (newCell.hasMinion()) {
                return false;
            }

            minion.setPosition(newX, newY);
            return true;
        } catch (Exception e) {
            throw new IOException("Error in MoveExpr.moveDirect: " + e.getMessage(), e);
        }
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        try {
            Player player = minion.getOwner();
            int budget = player.getBudget();

            if (budget < 1) {
                return 0;
            }

            if (moveDirect()) {
                player.setBudget(budget - 1);
                bindings.put("budget", budget - 1);
            }
            return 0;
        } catch (Exception e) {
            throw new Exception("Error in MoveExpr.eval: " + e.getMessage(), e);
        }
    }
}
