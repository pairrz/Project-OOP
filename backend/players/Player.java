package backend.players;

import backend.game.HexCell;
import backend.minions.Minion;
import java.util.ArrayList;

public interface Player {
    String getName();
    int getBudget();
    int getNumber();
    int getSumHP();
    int getRate(int turn);
    ArrayList<Minion> getMinions();
    ArrayList<HexCell> getHexCells();
    void buyMinion(int cost,Minion minion, boolean isMyHexCell);
    void buyHexCell(HexCell cell);
    void takeTurn(int turn);
    void removeMinion(int index);
    void setBudget(int budget);
    void setNumber(int i);
    void setSumHP(int i);
}
