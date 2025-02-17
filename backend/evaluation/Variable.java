package backend.evaluation;

import SyntaxErrorException.EvalError;
import backend.game.GameBoard;
import backend.game.GameConfig;
import backend.game.GameManage;
import backend.minions.Minion;
import backend.parser.Expr;
import backend.players.Player;

import java.util.Map;

public record Variable(String var, Player player, Minion minion, GameBoard board) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) {
        if (var.equals("row")) {
            return minion.getY();
        }else if(var.equals("col")) {
            return minion.getX();
        }else if(var.equals("budget")) {
            return player.getBudget();
        }else if (var.equals("int")) {
            return player.getRate(GameManage.turn);
        }else if(var.equals("maxbudget")){
            return GameConfig.MaxBudget;
        }else if(var.equals("spawnRemaining")){
            return board.getSpawnRemaining();
        }else if(var.equals("random")){
            int a = (int) Math.random();
            return (a % 1000);
        }

        if (bindings.containsKey(var)) {
            return bindings.get(var);
        }

        throw new EvalError("Undefined variable: " + var);
    }
}
