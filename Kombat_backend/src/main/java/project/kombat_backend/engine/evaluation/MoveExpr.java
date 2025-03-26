package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.parser.Expr;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.model.minion.Minion;
import project.kombat_backend.model.player.Player;

import java.io.IOException;
import java.util.Map;

public record MoveExpr(Minion minion, String direction, GameBoard gameBoard) implements Expr {

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

            if (!gameBoard.isValidPosition(newX, newY)) {
                return false;
            }

            HexCell newCell = gameBoard.getHexCellAt(newX, newY);
            if (newCell.hasMinion()) {
                return false;
            }

            HexCell newPosition = gameBoard.getHexCellAt(newX, newY);
            minion.setPosition(newPosition);
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
