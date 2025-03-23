package backend.players;

import backend.game.*;
import backend.minions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BotPlayer extends Player {

    public BotPlayer(String name, Map<String, HexCell> hexCells) {
        super(name, hexCells);
    }

    @Override
    public void takeTurn(int turn) throws IOException {
        printStatus();

        //buy Hex cell
        if (budget >= GameConfig.HexPurchase) {
            HexCell targetCell = findHexCell(turn);
            if (targetCell != null) {
                buyHexCell(targetCell);
            }
        }

        //buy Minion
        if(budget >= GameConfig.SpawnCost && minions.size() < 5) {
            HexCell spawnCell = findSpawnCell();
            if (spawnCell != null) {
                buyMinion(spawnCell);
            }
        }

        //minion strategy
        for (Minion minion : minions) {
            minion.minionStrategy("D:\\OOP project\\backend\\strategy\\Strategy3.txt");
        }

        printStatus();

        resetBudget(turn);
    }

    private HexCell findHexCell(int turn) {
        if(turn % 2 == 0){
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    HexCell cell = GameBoard.getHexCell(i,j);
                    if(isAdjacent(cell) && !GameBoard.isOccupied(i, j)){
                        return cell;
                    }
                }
            }
        }else{
            for(int i = 7; i >= 0; i--){
                for(int j = 7; j >= 0; j--){
                    HexCell cell = GameBoard.getHexCell(i,j);
                    if(isAdjacent(cell) && !GameBoard.isOccupied(i, j)){
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    private HexCell findSpawnCell() {
        if (hexCells.isEmpty()) {
            return null;
        }

        List<HexCell> cellList = new ArrayList<>(hexCells.values());
        Random random = new Random();
        HexCell cell = cellList.get(random.nextInt(cellList.size()));
        if(!cell.hasMinion()) return cell;
        else return null;
    }

    public void buyHexCell(HexCell targetCell) {
        budget -= GameConfig.HexPurchase;

        HexCell cell = GameBoard.getHexCell(targetCell.getX(), targetCell.getY());
        cell.setOwner(this);

        hexCells.put(targetCell.getX() + "," + targetCell.getY(), cell);
        System.out.println("HexCell (" + cell.getX() + "," + cell.getY() + ") ถูกซื้อสำเร็จ!");
    }

    @Override
    public void buyMinion(HexCell targetCell) {
        super.buyMinion(targetCell);
    }

    @Override
    public boolean isAdjacent(HexCell cell) {
        return super.isAdjacent(cell);
    }

    @Override
    public void printStatus() {
        super.printStatus();
    }

    @Override
    public Map<String, HexCell> getHexCells() {
        return super.getHexCells();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public int getBudget() {
        return super.getBudget();
    }

    @Override
    public int getNumMinions() {
        return super.getNumMinions();
    }

    @Override
    public ArrayList<Minion> getMinions() {
        return super.getMinions();
    }

    @Override
    public void resetBudget(int turn) {
        super.resetBudget(turn);
    }

    @Override
    public double getRate(int turn) {
        return super.getRate(turn);
    }

    @Override
    public int getSumHP() {
        return super.getSumHP();
    }
}