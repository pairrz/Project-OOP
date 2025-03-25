package backend.players;

import backend.game.*;
import backend.minions.*;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.min;
import static java.lang.Math.random;

public class BotPlayer extends Player {

    public BotPlayer(String name, Map<String, HexCell> hexCells) {
        super(name, hexCells);
    }

    @Override
    public void takeTurn(int turn) throws IOException, StrategyEvaluationException, StrategyProcessingException, InvalidStrategyException {
        printStatus();

        //buy Hex cell
        if (budget >= GameConfig.HexPurchase) {
            HexCell targetCell = findHexCell();
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
        if(!minions.isEmpty()) {
            for (Minion minion : minions) {
                minion.minionStrategy("D:\\OOP project\\backend\\strategy\\Strategy3.txt");
            }
        }

        printStatus();

        resetBudget(turn);
    }


    private HexCell findHexCell() {
        Set<HexCell> availableCells = new HashSet<>();

        for (HexCell cell : GameBoard.hexCellMap.values()) {
            if (!hexCells.containsKey(cell.getKey()) && isAdjacent(cell) && GameBoard.isValidPosition(cell.getX(), cell.getY())) {
                availableCells.add(cell);
            }
        }

        if (!availableCells.isEmpty()) {
            List<HexCell> availableList = new ArrayList<>(availableCells);

            Random random = new Random();
            int randomIndex = random.nextInt(availableList.size());

            return availableList.get(randomIndex);
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
    public void buyMinion(HexCell cell) {
        if (budget >= GameConfig.SpawnCost) {
            HexCell hexCell = GameBoard.getHexCell(cell.getX(), cell.getY());

            if (isMyHex(hexCell) && !hexCell.hasMinion()) {
                Minion minion = chooseType(hexCell);
                hexCell.addMinion(minion);
                budget -= GameConfig.SpawnCost;
                minions.add(minion);

                System.out.println("มินเนียนถูกวางใน HexCell (" + cell.getX() + "," + cell.getY() + ")");
            } else {
                System.out.println("ไม่สามารถวางมินเนียนที่นี่ได้!" + cell.getX() + "," + cell.getY());
            }
        } else {
            System.out.println("งบประมาณไม่พอ!");
        }
    }

//        return switch ((int) num) {
//            case 1 -> new LordMinion(this, cell);
//            case 2 -> new GiantMinion(this, cell);
//            case 3 -> new WarriorMinion(this, cell);
//            case 4 -> new HumanMinion(this, cell);
//            case 5 -> new SlaveMinion(this, cell);
//            default -> null;
//        };

    @Override
    public Minion chooseType(HexCell cell) {
        // ตรวจสอบว่า selectedMinions ไม่เป็น null และไม่ว่าง
        if (GameManage.selectedMinions == null || GameManage.selectedMinions.length == 0) {
            System.out.println("Error: No minions selected!");
            return null;
        }

        // สุ่มเลือกจาก selectedMinions
        Random rand = new Random();
        int index = rand.nextInt(GameManage.selectedMinions.length);
        int minionType = Integer.parseInt(GameManage.selectedMinions[index]);

        Minion minion = null;
        switch (minionType) {
            case 1 -> {
                minion = new LordMinion(this, cell);
                System.out.println("1. LordMinion selected");
            }
            case 2 -> {
                minion = new GiantMinion(this, cell);
                System.out.println("2. GiantMinion selected");
            }
            case 3 -> {
                minion = new WarriorMinion(this, cell);
                System.out.println("3. WarriorMinion selected");
            }
            case 4 -> {
                minion = new HumanMinion(this, cell);
                System.out.println("4. HumanMinion selected");
            }
            case 5 -> {
                minion = new SlaveMinion(this, cell);
                System.out.println("5. SlaveMinion selected");
            }
            default -> {
                System.out.println("Error: Invalid minion type selected");
                return null;
            }
        }
        return minion;
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