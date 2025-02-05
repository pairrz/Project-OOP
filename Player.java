import java.util.ArrayList;

public interface Player {
    String getName();
    int getBudget();
    int getNumber();
    int getSumHP();
    int getRate(int turn);
    ArrayList<Minion> getMinions();
    ArrayList<HexCell> getHexCells();
    void setBudget(int budget);
    void buyMinion(Minion minion,boolean isMyHexCell);
    void buyHexCell(HexCell cell);
    void takeTurn(int turn);
    void removeMinion(int index);

    void setNumber(int i);

    void setSumHP(int i);
}
