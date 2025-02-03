import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard {
    private HexCell[][] grid;
    private static GameBoard instance;
    private boolean[][] isOccupied;
    private ArrayList<HexCell> player1Hexes;
    private ArrayList<HexCell> player2Hexes;
    private int size;

    public GameBoard() {
        grid = new HexCell[size][size];
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                grid[i][j] = new HexCell(i, j);
            }
        }
        setupPlayerHexes();
    }

    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }


    private void setupPlayerHexes() {
        this.player1Hexes = new ArrayList<>();
        this.player2Hexes = new ArrayList<>();

        int k = 3;
        for (int i = 1; i <= 2; i++) {
            player1Hexes.addAll(Arrays.asList(grid[i]).subList(1, k + 1));
            k--;
        }

        k = 6;
        for (int i = 8; i >= 7; i--) {
            player2Hexes.addAll(Arrays.asList(grid[i]).subList(k, 9));
            k++;
        }
    }

    public ArrayList<HexCell> getPlayer1Hexes() {
        return player1Hexes;
    }

    public ArrayList<HexCell> getPlayer2Hexes() {
        return player2Hexes;
    }

    public HexCell getHexCell(int x, int y) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            return null;
        }
        return grid[x][y];
    }

    public boolean isOccupied(int x, int y) {
        HexCell cell = getHexCell(x, y);
        return cell != null && cell.isOccupied();
    }

    public void resetBoard() {
        initializeBoard();
    }

    private void initializeBoard() {
        grid = new HexCell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = new HexCell(i, j);
            }
        }
    }
}
