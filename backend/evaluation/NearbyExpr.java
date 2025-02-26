package backend.evaluation;

import backend.game.GameBoard;
import backend.minions.Minion;
import backend.parser.Expr;

import java.util.Map;

public record NearbyExpr(String direction, Minion minion) implements Expr {

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();
        int distance = 0;

        System.out.println("เริ่มค้นหามินเนียนในทิศทาง: " + direction);
        System.out.println("ตำแหน่งเริ่มต้น: (" + x + ", " + y + ")");

        // ลูปค้นหาตามทิศทางจนกว่าหลุดจากกระดาน
        while (GameBoard.isValidPosition(x, y) && (x < 8 && y < 8 && y >= 0 && x >= 0)) {
            Minion target = GameBoard.getHexCell(x, y).getMinion();

            System.out.println("ตรวจสอบที่: (" + x + ", " + y + ") -> " + (target != null ? "พบมินเนียน" : "ว่าง"));

            // ถ้าพบมินเนียนและไม่ใช่ตัวเอง
            if (target != null && target != minion) {
                int hpLength = String.valueOf(target.getHP()).length();
                int defLength = String.valueOf(target.getDef()).length();

                System.out.println("พบมินเนียนที่ระยะ: " + distance);

                if (target.getOwner() == minion.getOwner()) {
                    return -(100 * hpLength + 10 * defLength + distance);
                } else {
                    return (100 * hpLength + 10 * defLength + distance);
                }
            }

            // อัปเดตค่าพิกัดตามทิศทาง
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

        System.out.println("ไม่พบมินเนียนในทิศทาง: " + direction);
        return 0;
    }

}
