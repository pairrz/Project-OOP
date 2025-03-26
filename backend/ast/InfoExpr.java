package backend.ast;

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

        int[][] oddRowDirections = {
                {0, -1},  {1, -1},  {1, 0},  {0, 1},  {-1, 0},  {-1, -1}  //แถวคี่
        };
        int[][] evenRowDirections = {
                {0, -1},  {1, 0},  {1, 1},  {0, 1},  {-1, 1},  {-1, 0}  //แถวคู่
        };

        int[][] directions = (x % 2 == 0) ? evenRowDirections : oddRowDirections;

        int minDistance = Integer.MAX_VALUE;
        int bestResult = 0;
        Player player = minion.getOwner();

        for (int dir = 0; dir < 6; dir++) {
            int dx = directions[dir][0];
            int dy = directions[dir][1];

            int distance = 1;
            int newX = x + dx;
            int newY = y + dy;

            while (GameBoard.isValidPosition(newX, newY)) {
                Minion target = GameBoard.getHexCell(newX, newY).getMinion();

                if (target != null && target != minion) {
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
                newX += directions[dir][0];
                newY += directions[dir][1];
                distance++;
            }
        }
        bindings.put("x", bestResult);
        return bestResult;
    }
}
