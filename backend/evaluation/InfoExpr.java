package backend.evaluation;

import backend.game.GameBoard;
import backend.minions.Minion;
import backend.parser.Expr;
import backend.players.Player;

import java.util.Map;

public record InfoExpr(String type, Minion minion) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();

        boolean findAlly = type.equals("ally");
        boolean findOpponent = type.equals("opponent");

        if (!findAlly && !findOpponent) {
            throw new IllegalArgumentException("Invalid info type: " + type);
        }

        int minDistance = Integer.MAX_VALUE;
        int bestResult = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int newX = x + dx;
                int newY = y + dy;
                int distance = 1;

                while (GameBoard.isValidPosition(newX, newY)) {
                    Minion target = GameBoard.getHexCell(newX, newY).getMinion();

                    if (target != null) {
                        boolean isAlly = target.getOwner() == minion.getOwner();
                        boolean isOpponent = target.getOwner() != minion.getOwner();

                        if ((findAlly && isAlly) || (findOpponent && isOpponent)) {
                            if (distance < minDistance) {
                                minDistance = distance;
                                bestResult = 100 * target.getHP() + 10 * target.getDef() + distance;
                            }
                            break;
                        }
                    }
                    newX += dx;
                    newY += dy;
                    distance++;
                }
            }
        }
        return bestResult;
    }
}
