package backend.evaluation;

import SyntaxErrorException.EvalError;
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
                int a = (int) Math.random();
                return (a % 1000);
            }
        }

        if (bindings.containsKey(var)) {
            return bindings.get(var);
        }

        throw new EvalError("Undefined variable: " + var);
    }
}
