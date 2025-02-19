package backend.evaluation;

import backend.game.GameBoard;
import backend.game.HexCell;
import backend.minions.Minion;
import backend.parser.Expr;

import java.util.Map;

public record MoveExpr(Minion minion, String direction, GameBoard board) implements Expr {
    public boolean moveDirect() {
        int x = minion.getX();
        int y = minion.getY();
        int newX = x, newY = y;

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

        // ตรวจสอบว่าตำแหน่งใหม่อยู่ในขอบเขตบอร์ด
        if (!board.isValidPosition(newX, newY)) {
            System.out.println("ตำแหน่งใหม่อยู่นอกบอร์ด!");
            return false;
        }

        // ตรวจสอบว่าตำแหน่งใหม่ถูกยึดครองหรือไม่
        HexCell newCell = GameBoard.getHexCell(newX, newY);
        if (newCell.isOccupied()) {
            System.out.println("ตำแหน่งนี้มีมินเนียนอยู่แล้ว ไม่สามารถย้ายได้!");
            return false;
        }

        // ใช้ setPosition เพื่ออัปเดตตำแหน่งของมินเนียน
        minion.setPosition(newX, newY);

        return true;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int budget = bindings.getOrDefault("budget", 0);
        if (budget < 1) {
            return 0;
        }

        if (moveDirect()) {
            bindings.put("budget", budget - 1);
        }

        return 0;
    }
}
