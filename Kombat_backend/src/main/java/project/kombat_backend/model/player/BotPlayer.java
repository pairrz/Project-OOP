package project.kombat_backend.model.player;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.model.minion.Minion;
import java.util.*;

@Entity
public class BotPlayer extends Player {
    public BotPlayer() {}

    @Transient
    private GameConfig gameConfig = getGameConfig();

    public BotPlayer(String name ,GameConfig gameConfig) {
        super(name , gameConfig);
    }

    public void takeTurn(int turn) {
        GameBoard gameBoard = getGameBoard();

        if (getBudget() >= getGameConfig().getHexPurchaseCost()) {
            HexCell targetCell = findHexCell(gameBoard);
            if (targetCell != null) {
                buyHexCell(targetCell);
            }
        }

        if (getBudget() >= getGameConfig().getSpawnCost() && getNumMinions() < 5) {
            HexCell spawnCell = findSpawnCell();
            if (spawnCell != null) {
                buyMinion(spawnCell);
            }
        }

        resetBudget(turn);
    }

    private HexCell findHexCell(GameBoard gameBoard) {
        List<HexCell> availableCells = new ArrayList<>();

        for (HexCell cell : gameBoard.getHexCellMap()) {
            if (isAdjacent(cell) && cell.getOwner() == null) {
                availableCells.add(cell);
            }
        }

        return availableCells.isEmpty() ? null : availableCells.get(new Random().nextInt(availableCells.size()));
    }

    private boolean isAdjacent(HexCell cell) {
        int x = cell.getX();
        int y = cell.getY();

        int[][] directions = (y % 2 == 0) ?
                new int[][]{{-1, 0}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}} :
                new int[][]{{-1, 0}, {-1, 1}, {0, 1}, {1, 0}, {0, -1}, {-1, -1}};

        return getHexCells().stream().anyMatch(ownedCell ->
                Arrays.stream(directions).anyMatch(dir ->
                        ownedCell.getX() == x + dir[0] && ownedCell.getY() == y + dir[1]));
    }

    private HexCell findSpawnCell() {
        List<HexCell> ownedCells = new ArrayList<>(getHexCells());
        Collections.shuffle(ownedCells);
        return ownedCells.stream().filter(cell -> !cell.hasMinion()).findFirst().orElse(null);
    }

    public void buyMinion(HexCell cell) {
        if (getBudget() < gameConfig.getSpawnCost()) {
            System.out.println("งบประมาณไม่พอ!");
            return;
        }

        HexCell hexCell = getGameBoard().getHexCellAt(cell.getX(), cell.getY());

        if (hexCell.getOwner() == this) {
            Minion minion = new Minion(this, hexCell,gameConfig);
            hexCell.setMinion(minion);

            setBudget(getBudget() - gameConfig.getSpawnCost());
            getMinions().add(minion);
            System.out.println("มินเนียนถูกวางใน HexCell (" + cell.getX() + "," + cell.getY() + ")");
        } else {
            System.out.println("ไม่สามารถวางมินเนียนที่นี่ได้!");
        }
    }

    public void buyHexCell(HexCell targetCell) {
        if (getBudget() < gameConfig.getHexPurchaseCost()) return;

        setBudget(getBudget() - gameConfig.getHexPurchaseCost());
        targetCell.setOwner(this);
        getHexCells().add(targetCell);
    }
}
