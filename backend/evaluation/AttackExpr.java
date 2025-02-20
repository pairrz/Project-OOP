package backend.evaluation;

import backend.game.*;
import backend.parser.*;
import backend.minions.*;
import backend.players.*;

import java.util.Map;

public record AttackExpr(Minion attacker, String direction, Expr expend) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int budget = attacker.getOwner().getBudget();
        int expenditure = expend.eval(bindings);
        int totalCost = expenditure + 1;

        if (budget < totalCost) {
            return 0;
        }
        attacker.getOwner().setBudget(budget - totalCost);
        bindings.put("budget", budget - totalCost);

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

        HexCell targetCell = GameBoard.getHexCell(targetX, targetY);
        if (targetCell == null || targetCell.hasMinion()) {
            System.out.println("Target cell is null!");
            return 0;
        }

        Minion targetMinion = targetCell.getMinion();
        int hp = targetMinion.getHP();
        int defense = targetMinion.getDef();
        int damage = Math.max(1, expenditure - defense);
        targetMinion.setHP(Math.max(0, hp - damage));

        return damage;
    }
}

