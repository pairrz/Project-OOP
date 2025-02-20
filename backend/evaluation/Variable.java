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

        switch (var) {
            case "row" -> {
                return minion.getY() + 1;
            }
            case "col" -> {
                return minion.getX() + 1;
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
                Random rand = new Random();
                int a = rand.nextInt();
                return (a % 1000);
            }
        }

        if (bindings.containsKey(var)) {
            return bindings.get(var);
        }
        throw new EvalError("Undefined variable: " + var);
    }
}
