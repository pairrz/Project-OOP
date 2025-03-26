package project.kombat_backend.engine.evaluation;

import project.kombat_backend.engine.parser.Expr;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.minion.Minion;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.service.GameManager;

import java.util.Map;

public record Variable(String var, Minion minion, GameBoard gameBoard, GameManager gameManager) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        Player player = minion.getOwner();
        int currentTurn = gameManager.getCurrentTurn();
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
                return (int) player.getRate(currentTurn);
            }
            case "maxbudget" -> {
                return gameBoard.getGameConfig().getMaxBudget();
            }
            case "spawnRemaining" -> {
                return gameBoard.getSpawnRemaining();
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

