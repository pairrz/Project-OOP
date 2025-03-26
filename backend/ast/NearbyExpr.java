package backend.ast;

import backend.game.GameBoard;
import backend.minions.Minion;
import backend.parser.Expr;
import java.util.Map;

public record NearbyExpr(String direction, Minion minion) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();
        int distance = 1;

        while (GameBoard.isValidPosition(x, y) && (x < 8 && y < 8 && y >= 0 && x >= 0)) {
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

            Minion target = GameBoard.getHexCell(x, y).getMinion();

            if (target != null && target != this.minion) {
                int hpLength = String.valueOf(target.getHP()).length();
                int defLength = String.valueOf(target.getDef()).length();

                if (target.getOwner() == minion.getOwner()) {
                    return -(100 * hpLength + 10 * defLength + distance); //ally
                } else {
                    return (100 * hpLength + 10 * defLength + distance); //opponent
                }
            }
            distance++;
        }
        return 0;
    }

}
