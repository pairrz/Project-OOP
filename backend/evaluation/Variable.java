package backend.evaluation;

import SyntaxErrorException.EvalError;
import backend.game.*;
import backend.minions.*;
import backend.parser.*;
import backend.players.*;

import java.util.Map;
import java.util.Random;

public record Variable(String var, Minion minion) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) {
        Player player = minion.getOwner();

        return switch (var) {
            case "row" -> minion.getY() + 1;
            case "col" -> minion.getX() + 1;
            case "budget" -> player.getBudget();
            case "int" -> player.getRate(GameManage.turn);
            case "maxbudget" -> GameConfig.MaxBudget;
            case "spawnRemaining" -> GameBoard.getSpawnRemaining();
            case "random" -> new Random().nextInt(1000);
            default -> bindings.computeIfAbsent(var, k -> 0);
        };
    }
}


