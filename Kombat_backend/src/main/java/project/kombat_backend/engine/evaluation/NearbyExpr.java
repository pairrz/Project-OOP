package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.parser.Expr;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.minion.Minion;

import java.util.Map;

public record NearbyExpr(String direction, Minion minion, GameBoard gameBoard) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();
        int distance = 1;

        while (gameBoard.isValidPosition(x, y)) {
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

            Minion target = gameBoard.getHexCellAt(x, y).getMinion();

            if (target != null && target != this.minion) {
                int hpLength = String.valueOf(target.getHp()).length();
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
