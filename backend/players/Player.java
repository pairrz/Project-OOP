package backend.players;

import backend.game.HexCell;
import backend.minions.Minion;

import java.io.IOException;
import java.util.ArrayList;

public interface Player {
    String getName();
    int getBudget();
    int getNumber();
    int getSumHP();
    int getRate(int turn);
    ArrayList<Minion> getMinions();
    ArrayList<HexCell> getHexCells();
    boolean isAdjacent(HexCell cell);
    boolean isMyHexCell(HexCell cell);
    void buyMinion(HexCell cell);
    void buyHexCell();
    void takeTurn(int turn) throws IOException;
    void removeMinion(Minion minion);
    void setBudget(int budget);
    void setNumber(int i);
    void setSumHP(int i);
}
