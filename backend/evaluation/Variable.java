package backend.evaluation;

import backend.game.GameBoard;
import backend.game.GameConfig;
import backend.game.GameManage;
import backend.minions.Minion;
import backend.parser.Expr;
import backend.players.Player;

import java.util.Map;

public record Variable(String var, Minion minion) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) {
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
                return player.getRate(GameManage.turn);
            }
            case "maxbudget" -> {
                return GameConfig.MaxBudget;
            }
            case "spawnRemaining" -> {
                return GameBoard.getSpawnRemaining();
            }
            case "random" -> {
                return (int) (Math.random() * 1000); // แก้ไขให้สุ่มค่าระหว่าง 0-999
            }
            default -> {
                bindings.putIfAbsent(var, 0);
                return bindings.get(var);
            }
        }
    }
}

