import java.util.ArrayList;

public class HumanPlayer implements Player {
    String name;
    int budget;
    ArrayList<HexCell> hexCells;
    ArrayList<Minion> minions;
    int numMinions;
    int baseR;

    public HumanPlayer(String name) {
        this.name = name;
        this.budget = GameRule.InitBudget;
        this.hexCells = new ArrayList<>();
        this.minions = new ArrayList<>();
        this.numMinions = 0;
        this.baseR = GameRule.InterestPct;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getBudget() {
        return budget;
    }

    @Override
    public int getNumber() {
        return numMinions;
    }

    @Override
    public int getSumHP() {
        int sum = 0;
        for(Minion minion : minions) {
            sum += minion.getHP();
        }
        return sum;
    }

    @Override
    public int getRate(int turn) {
        return (int) (baseR * Math.log10(Double.valueOf(budget)) * Math.log(Double.valueOf(turn)));
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
    public void setBudget(int turn) {
        if(turn > 1){
            int result = budget * (int) getRate(turn) / 100;
            budget = result;
        }
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
    public void takeTurn(int turn) {
        for(Minion minion : minions) {
            minion.minionStrategy("D:\\OOP project\\Strategy.txt");
        }
    }

    @Override
    public void removeMinion(int index) {
        minions.remove(index);
        numMinions--;
    }

    @Override
    public void setNumber(int i) {

    }

    @Override
    public void setSumHP(int i) {

    }


}
