package backend.evaluation;

import SyntaxErrorException.DoneException;
import SyntaxErrorException.EvalError;
import backend.game.GameManage;
import backend.game.GameRule;
import backend.minions.Minion;
import backend.players.Player;

import java.util.Map;

record AssignmentExpr(String var, Expr expr) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int result = expr.eval(bindings);
        bindings.put(var, result);
        return result;
    }
}

record AttackExpr(Player player, Minion attacker, String direction, Expr expend, GameBoard board) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int budget = player.getBudget();
        int expenditure = expend.eval(bindings);
        int totalCost = expenditure + 1;

        if (budget < totalCost) {
            return 0;
        }

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

        HexCell targetCell = board.getHexCell(targetX, targetY);
        if (targetCell == null) {
            return 0;
        }

        Minion targetMinion = targetCell.getMinion();

        bindings.put("budget", budget - totalCost);

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
            player.removeMinion(targetMinion.getIndex());
        }
        return 0;
    }
}

record BinaryArithExpr(Expr left, String op, Expr right) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int lv = left.eval(bindings);
        int rv = right.eval(bindings);

        if ("/".equals(op) || "%".equals(op)) {
            if (rv == 0) throw new EvalError("Division by zero");
        }

        return switch (op) {
            case "+" -> lv + rv;
            case "-" -> lv - rv;
            case "*" -> lv * rv;
            case "/" -> lv / rv;
            case "%" -> lv % rv;
            case "^" -> (int) Math.pow(lv, rv);
            default -> throw new EvalError("Unknown operator: " + op);
        };
    }
}

record DoneExpr() implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        throw new DoneException();
    }
}

record IfStatementExpr(Expr condition, Expr thenExpr, Expr elseExpr) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        if (condition.eval(bindings) > 0) {
            return thenExpr.eval(bindings);
        } else {
            return elseExpr.eval(bindings);
        }
    }
}

record InfoExpr(String type, Player player, Minion minion, GameBoard board) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();

        boolean findAlly = type.equals("ally");
        boolean findOpponent = type.equals("opponent");

        if (!findAlly && !findOpponent) {
            throw new IllegalArgumentException("Invalid info type: " + type);
        }

        int[][] directions = {
                {0, -1},
                {-1, -1},
                {-1, 0},
                {1, 0},
                {1, 1},
                {0, 1}
        };

        int minDistance = Integer.MAX_VALUE;
        int bestResult = 0;

        for (int dir = 0; dir < directions.length; dir++) {
            int dx = directions[dir][0];
            int dy = directions[dir][1];

            int distance = 1;
            int newX = x + dx;
            int newY = y + dy;

            while (board.isValidPosition(newX, newY)) {
                Minion target = board.getHexCell(newX, newY).getMinion();

                if (target != null) {
                    boolean isAlly = target.getOwner() == player;
                    boolean isOpponent = target.getOwner() != player;

                    if ((findAlly && isAlly) || (findOpponent && isOpponent)) {
                        int locationValue = distance * 10 + (dir + 1);
                        if (distance < minDistance || (distance == minDistance && locationValue < bestResult)) {
                            minDistance = distance;
                            bestResult = locationValue;
                        }
                        break;
                    }
                }
                newX += dx;
                newY += dy;
                distance++;
            }
        }
        return bestResult;
    }
}

record IntLit(int val) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) {
        return val;
    }
}

record MoveExpr(Minion minion, String direction, GameBoard board) implements Expr {
    public boolean moveDirect() {
        int x = minion.getX();
        int y = minion.getY();
        int newX = x, newY = y;

        switch (direction) {
            case "up": newX--; break;
            case "down": newX++; break;
            case "upleft": newX--; newY--; break;
            case "upright": newX--; newY++; break;
            case "downleft": newY--; break;
            case "downright": newY++; break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        HexCell currentCell = board.getHexCell(x, y);
        HexCell newCell = board.getHexCell(newX, newY);

        if (newCell == null || newCell.isOccupied()) {
            return false;
        }
        currentCell.removeMinion();
        newCell.setMinion(minion);
        minion.setPosition(newX, newY);

        return true;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int budget = bindings.getOrDefault("budget", 0);
        if (budget < 1) {
            return 0;
        }

        if (moveDirect()) {
            bindings.put("budget", budget - 1);
        }

        return 0;
    }
}

record NearbyExpr(String direction, Player player, Minion minion, GameBoard board) implements Expr {

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int x = minion.getX();
        int y = minion.getY();
        int distance = 0;

        while (board.isValidPosition(x, y)) {
            Minion target = board.getHexCell(x, y).getMinion();

            if (target != null) {
                int hpLength = String.valueOf(target.getHP()).length();
                int defenseLength = String.valueOf(target.getDef()).length();
                int minionDistance = distance;

                if (target.getOwner() == player) {
                    return -(100 * hpLength + 10 * defenseLength + minionDistance);
                } else {
                    return (100 * hpLength + 10 * defenseLength + minionDistance);
                }
            }

            switch (direction) {
                case "up":
                    x--;
                    break;
                case "down":
                    x++;
                    break;
                case "upleft":
                    x--;
                    y--;
                    break;
                case "upright":
                    x--;
                    y++;
                    break;
                case "downleft":
                    x++;
                    y--;
                    break;
                case "downright":
                    x++;
                    y++;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid direction: " + direction);
            }
            distance++;
        }
        return 0;
    }
}

record Variable(String var,Player player, Minion minion,GameBoard board) implements Expr {
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
            return GameRule.MaxBudget;
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

record WhileStatementExpr(Expr condition, Expr statement) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        int result = 0;
        while (condition.eval(bindings) > 0){
            result = statement.eval(bindings);
        }
        return result;
    }
}