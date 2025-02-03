import java.util.Map;

public class AttackExpr implements Expr {
    private String direction;
    private Expr expenditureExpr;
    private Minion attacker;
    private GameBoard board;

    public AttackExpr(Minion attacker, String direction, Expr expenditureExpr, GameBoard board) {
        this.attacker = attacker;
        this.direction = direction;
        this.expenditureExpr = expenditureExpr;
        this.board = board;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int budget = bindings.getOrDefault("budget", 0);
        int expenditure = expenditureExpr.eval(bindings); // ค่าโจมตีที่ผู้เล่นเลือก
        int totalCost = expenditure + 1;

        // ถ้างบไม่พอ → ไม่สามารถโจมตีได้
        if (budget < totalCost) {
            return 0;
        }

        int x = attacker.getX();
        int y = attacker.getY();
        int targetX = x, targetY = y;

        // คำนวณตำแหน่งเป้าหมายตาม direction
        switch (direction) {
            case "up": targetX--; break;
            case "down": targetX++; break;
            case "upleft": targetX--; targetY--; break;
            case "upright": targetX--; targetY++; break;
            case "downleft": targetY--; break;
            case "downright": targetY++; break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        HexCell targetCell = board.getHexCell(targetX, targetY);
        if (targetCell == null) {
            return 0; // เป้าหมายอยู่นอกกระดาน → ไม่สามารถโจมตีได้
        }

        Minion targetMinion = targetCell.getMinion();

        // หักงบประมาณ
        bindings.put("budget", budget - totalCost);

        if (targetMinion == null) {
            return 0; // เป้าหมายว่างเปล่า → โจมตีไม่ส่งผลอะไร
        }

        int hp = targetMinion.getHP();
        int defense = targetMinion.getDefense();
        int damage = Math.max(1, expenditure - defense);
        int newHP = Math.max(0, hp - damage);

        // อัปเดต HP ของเป้าหมาย
        targetMinion.setHP(newHP);

        // ถ้า HP = 0 → มินเนียนตาย
        if (newHP == 0) {
            targetCell.removeMinion();
        }

        return 0;
    }
}
