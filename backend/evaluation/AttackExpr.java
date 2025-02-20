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

        bindings.put("budget", budget - totalCost);

        int targetX = attacker.getX();
        int targetY = attacker.getY();

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

        HexCell targetCell = GameBoard.getHexCell(targetX, targetY);
        if (targetCell == null) {
            return 0;
        }

        Minion targetMinion = targetCell.getMinion();

        if (targetMinion == null) {
            return 0;
        }

        int hp = targetMinion.getHP();
        int defense = targetMinion.getDef();
        int damage = Math.max(1, expenditure - defense);
        int newHP = Math.max(0, hp - damage);

        targetMinion.setHP(newHP);

        if (newHP == 0) {
            targetCell.removeMinion();
            Player player = targetMinion.getOwner();
            player.removeMinion(targetMinion);
        }
        return 0;
    }
}

