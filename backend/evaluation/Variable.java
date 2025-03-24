package backend.evaluation;

import backend.game.*;
import backend.minions.*;
import backend.parser.*;
import backend.players.*;
import java.util.Map;

public record Variable(String var, Minion minion) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        Player player = minion.getOwner();
        switch (var) {
            case "row" -> {
                return minion.getY();
            }
            case "col" -> {
                return minion.getX();
            }
            case "budget" -> {
                return player.getBudget();
            }
            case "int" -> {
                return (int) player.getRate(GameManage.turn);
            }
            case "maxbudget" -> {
                return GameConfig.MaxBudget;
            }
            case "spawnRemaining" -> {
                return GameBoard.getSpawnRemaining();
            }
            case "random" -> {
                return (int) (Math.random() * 1000); //สุ่มค่า 0-999
            }
            default -> {
                bindings.putIfAbsent(var, 0);
                return bindings.get(var);
            }
        }
    }
}

