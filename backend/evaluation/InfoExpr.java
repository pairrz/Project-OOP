package backend.evaluation;

import backend.game.GameBoard;
import backend.minions.Minion;
import backend.parser.Expr;
import backend.players.Player;

import java.util.Map;

public record InfoExpr(String type, Player player, Minion minion, GameBoard board) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();

        boolean findAlly = type.equals("ally");
        boolean findOpponent = type.equals("opponent");

        if (!findAlly && !findOpponent) {
            throw new IllegalArgumentException("Invalid info type: " + type);
        }

        int[][] directions = {
                {0, -1},
                {-1, -1},
                {-1, 0},
                {1, 0},
                {1, 1},
                {0, 1}
        };

        int minDistance = Integer.MAX_VALUE;
        int bestResult = 0;

        for (int dir = 0; dir < directions.length; dir++) {
            int dx = directions[dir][0];
            int dy = directions[dir][1];

            int distance = 1;
            int newX = x + dx;
            int newY = y + dy;

            while (board.isValidPosition(newX, newY)) {
                Minion target = board.getHexCell(newX, newY).getMinion();

                if (target != null) {
                    boolean isAlly = target.getOwner() == player;
                    boolean isOpponent = target.getOwner() != player;

                    if ((findAlly && isAlly) || (findOpponent && isOpponent)) {
                        int locationValue = distance * 10 + (dir + 1);
                        if (distance < minDistance || (distance == minDistance && locationValue < bestResult)) {
                            minDistance = distance;
                            bestResult = locationValue;
                        }
                        break;
                    }
                }
                newX += dx;
                newY += dy;
                distance++;
            }
        }
        return bestResult;
    }
}
