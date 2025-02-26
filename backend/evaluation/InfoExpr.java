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

        System.out.println("Minion Position: (" + x + ", " + y + ")");

        boolean findAlly = type.equals("ally");
        boolean findOpponent = type.equals("opponent");

        if (!findAlly && !findOpponent) {
            throw new IllegalArgumentException("Invalid info type: " + type);
        }

        int[][] directions = {
                {0, -1},  // 1 - Up
                {1, -1},  // 2 - UpRight
                {1, 0},   // 3 - DownRight
                {0, 1},   // 4 - Down
                {-1, 1},  // 5 - DownLeft
                {-1, 0}   // 6 - UpLeft
        };

        int minDistance = Integer.MAX_VALUE;
        int bestResult = 0;

        Player player = minion.getOwner();

        for (int dir = 0; dir < directions.length; dir++) {
            int dx = directions[dir][0];
            int dy = directions[dir][1];

            int distance = 1;
            int newX = x + dx;
            int newY = y + dy;

            while (GameBoard.isValidPosition(newX, newY)) {
                Minion target = GameBoard.getHexCell(newX, newY).getMinion();
                System.out.println("Checking Position: (" + newX + ", " + newY + ")");

                if (target != null) {
                    boolean isAlly = target.getOwner() == player;
                    boolean isOpponent = target.getOwner() != player;

                    if ((findAlly && isAlly) || (findOpponent && isOpponent)) {
                        int locationValue = distance * 10 + (dir + 1);
                        System.out.println("Found target at distance " + distance + ", direction " + (dir + 1) + ", value: " + locationValue);

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

        bindings.put("x", bestResult);
        System.out.println("Final result: " + bestResult);
        return bestResult;
    }
}
