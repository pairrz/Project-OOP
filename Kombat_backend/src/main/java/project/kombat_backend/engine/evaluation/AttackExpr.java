package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.parser.Expr;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.model.minion.Minion;
import project.kombat_backend.model.player.Player;
import java.util.Map;

public class AttackExpr implements Expr {
    private final Minion attacker;
    private final String direction;
    private final Expr expend;
    private final GameBoard gameBoard;

    public AttackExpr(Minion attacker, String direction, Expr expend, GameBoard gameBoard) {
        this.attacker = attacker;
        this.direction = direction;
        this.expend = expend;
        this.gameBoard = gameBoard;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        try {
            Player player = attacker.getOwner();
            int budget = player.getBudget();
            int expenditure = expend.eval(bindings);
            int totalCost = expenditure + 1;

            if (budget < totalCost) return 0;

            int targetX = attacker.getX();
            int targetY = attacker.getY();

            switch (direction) {
                case "up" -> targetX--;
                case "down" -> targetX++;
                case "upleft" -> { targetX--; targetY--; }
                case "upright" -> { targetX--; targetY++; }
                case "downleft" -> targetY--;
                case "downright" -> targetY++;
                default -> throw new IllegalArgumentException("Invalid direction: " + direction);
            }

            player.setBudget(budget - totalCost);

            HexCell targetCell = gameBoard.getHexCellAt(targetX, targetY);
            if (targetCell == null || targetCell.getMinion() == null) return 0;

            Minion targetMinion = targetCell.getMinion();
            int hp = targetMinion.getHp();
            int defense = targetMinion.getDef();
            int damage = Math.max(1, expenditure - defense);
            int newHP = Math.max(0, hp - damage);

            System.out.println(targetMinion.getOwner().getName() + "'s minion at (" + targetMinion.getX() + ", " + targetMinion.getY() + ") was attacked.");
            targetMinion.setHp(newHP);

            if (newHP == 0) {
                System.out.println(targetMinion.getOwner().getName() + "'s minion at (" + targetMinion.getX() + ", " + targetMinion.getY() + ") died.");
                targetCell.removeMinion();
                targetMinion.getOwner().removeMinion(targetMinion);
            }
            return 0;
        } catch (Exception e) {
            throw new Exception("Error in AttackExpr: " + e.getMessage(), e);
        }
    }
}