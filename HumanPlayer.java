import java.util.ArrayList;

public class HumanPlayer implements Player {
    String name;
    int budget;
    ArrayList<HexCell> hexCells;
    ArrayList<Minion> minions;
    int numMinions;

    public HumanPlayer(String name) {
        this.name = name;
        this.budget = GameRule.InitBudget;
        this.hexCells = new ArrayList<>();
        this.minions = new ArrayList<>();
        this.numMinions = 0;
    }

    @Override
    public int getBudget() {
        return budget;
    }

    @Override
    public ArrayList<Minion> getMinions() {
        return minions;
    }

    @Override
    public ArrayList<HexCell> getHexCells() {
        return hexCells;
    }

    @Override
    public boolean isPlayerOne() {
        return false;
    }

    @Override
    public boolean isPlayerTwo() {
        return false;
    }

    @Override
    public void buyMinion(Minion minion, boolean isMyHexCell) {
        if(budget >= GameRule.SpawnCost) {
            budget -= GameRule.SpawnCost;
            minions.add(minion);
            numMinions++;
        }

    }

    @Override
    public void buyHexCell(HexCell cell) {
        hexCells.add(cell);
    }

    @Override
    public void takeTurn() {

    }

    @Override
    public void removeMinion(int index) {
        minions.remove(index);
        numMinions--;
    }
}
