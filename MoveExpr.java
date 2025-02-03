import java.util.Map;

public class MoveExpr implements Expr {
    private Minion minion;
    private String direction;
    private GameBoard board;

    public MoveExpr(Minion minion, String direction, GameBoard board) {
        this.minion = minion;
        this.direction = direction;
        this.board = board;
    }

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

        HexCell currentCell = board.getHexCell(x, y);
        HexCell newCell = board.getHexCell(newX, newY);

        if (newCell == null || newCell.isOccupied()) {
            return false;
        }
        currentCell.removeMinion();
        newCell.setMinion(minion);
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
