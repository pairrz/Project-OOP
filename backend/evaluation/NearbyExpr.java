package backend.evaluation;

import backend.game.GameBoard;
import backend.minions.Minion;
import backend.parser.Expr;
import backend.players.Player;

import java.util.Map;

public record NearbyExpr(String direction, Player player, Minion minion, GameBoard board) implements Expr {

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();
        int distance = 0;

        while (board.isValidPosition(x, y)) {
            Minion target = board.getHexCell(x, y).getMinion();

            if (target != null) {
                int hpLength = String.valueOf(target.getHP()).length();
                int defenseLength = String.valueOf(target.getDef()).length();
                int minionDistance = distance;

                if (target.getOwner() == player) {
                    return -(100 * hpLength + 10 * defenseLength + minionDistance);
                } else {
                    return (100 * hpLength + 10 * defenseLength + minionDistance);
                }
            }

            switch (direction) {
                case "up":
                    x--;
                    break;
                case "down":
                    x++;
                    break;
                case "upleft":
                    x--;
                    y--;
                    break;
                case "upright":
                    x--;
                    y++;
                    break;
                case "downleft":
                    x++;
                    y--;
                    break;
                case "downright":
                    x++;
                    y++;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid direction: " + direction);
            }
            distance++;
        }
        return 0;
    }
}
