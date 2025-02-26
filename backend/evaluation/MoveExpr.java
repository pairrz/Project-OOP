package backend.evaluation;

import backend.game.GameBoard;
import backend.game.HexCell;
import backend.minions.Minion;
import backend.parser.Expr;
import backend.players.Player;

import java.io.IOException;
import java.util.Map;

public record MoveExpr(Minion minion, String direction) implements Expr {

    public boolean moveDirect() throws IOException {
        System.out.println("move eval");

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
        if (!GameBoard.isValidPosition(newX, newY)) {
            System.out.println("ตำแหน่งใหม่อยู่นอกบอร์ด!");
            return false;
        }

        // ตรวจสอบว่าตำแหน่งใหม่ถูกยึดครองหรือไม่
        HexCell newCell = GameBoard.getHexCell(newX, newY);
        if (newCell.hasMinion()) {
            System.out.println("ตำแหน่งนี้มีมินเนียนอยู่แล้ว ไม่สามารถย้ายได้!");
            return false;
        }
        // ใช้ setPosition เพื่ออัปเดตตำแหน่งของมินเนียน
        minion.setPosition(newX, newY);

        return true;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        System.out.println("eval move");
        Player player = minion.getOwner();
        int budget = player.getBudget();

        if (budget < 1) {
            return 0;
        }

        if (moveDirect()) {
            player.setBudget(budget - 1);  // อัปเดต budget ของ Player จริง
            bindings.put("budget", budget - 1); // อัปเดต bindings เพื่อให้ตรงกับค่าปัจจุบัน
        }

        return 0;
    }
}
