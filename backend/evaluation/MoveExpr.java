package backend.evaluation;

import backend.game.GameBoard;
import backend.game.HexCell;
import backend.minions.Minion;
import backend.parser.Expr;
import backend.players.Player;

import java.util.Map;

public record MoveExpr(Minion minion, String direction) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int newX = minion.getX();
        int newY = minion.getY();
        Player player = minion.getOwner();

        if (player.getBudget() < 1) {
            return 0;
        }

        switch (direction) {
            case "up" -> newX--;
            case "down" -> newX++;
            case "upleft" -> { newX--; newY--; }
            case "upright" -> { newX--; newY++; }
            case "downleft" -> newY--;
            case "downright" -> newY++;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        if (!GameBoard.isValidPosition(newX, newY)) {
            System.out.println("Invalid position: " + newX + ", " + newY);
            return 0;
        }

        HexCell newCell = GameBoard.getHexCell(newX, newY);
        if (newCell.hasMinion()) {
            System.out.println("Minion already exists: " + newCell);
            return 0;
        }

        // ลด budget และย้ายตำแหน่งมินเนียน
        player.setBudget(player.getBudget() - 1);
        System.out.println("ลดในมูฟ");
        bindings.put("budget", player.getBudget());
        minion.moveTo(newX, newY);

        return 1;
    }
}