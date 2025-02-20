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
            System.out.println("งบประมาณไม่เพียงพอ!");
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
            System.out.println("ตำแหน่งใหม่อยู่นอกบอร์ด!");
            return 0;
        }

        HexCell newCell = GameBoard.getHexCell(newX, newY);
        if (newCell.hasMinion()) {
            System.out.println("ตำแหน่งนี้มีมินเนียนอยู่แล้ว ไม่สามารถย้ายได้!");
            return 0;
        }

        // ลด budget และย้ายตำแหน่งมินเนียน
        player.setBudget(player.getBudget() - 1);
        minion.moveTo(newX, newY);

        return 1;
    }
}