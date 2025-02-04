import java.util.ArrayList;

public interface Player {
    int getBudget();
    ArrayList<Minion> getMinions();
    ArrayList<HexCell> getHexCells();
    boolean isPlayerOne();
    boolean isPlayerTwo();
    void buyMinion(Minion minion,boolean isMyHexCell);
    void buyHexCell(HexCell cell);
    void takeTurn();
    void removeMinion(int index);
}
