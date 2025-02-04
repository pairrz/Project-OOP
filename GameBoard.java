import java.util.ArrayList;


public class GameBoard {
    private static GameBoard instance;
    private HexCell[][] grid;
    private int size = 8;
    private boolean[][] isOccupied = new boolean[8][8];

    private Player playerOne, playerTwo;
    private Player currentPlayer;
    private ArrayList<HexCell> player1Hexes;
    private ArrayList<HexCell> player2Hexes;


    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public GameBoard() {
        playerOne = new HumanPlayer("Player One");
        playerTwo = new HumanPlayer("Player Two");
        currentPlayer = playerOne;

        grid = new HexCell[size][size];

        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                grid[i][j] = new HexCell(i, j);
                isOccupied[i][j] = false;
            }
        }
        setupPlayerHexes();
    }

    private void setupPlayerHexes() {
        this.player1Hexes = new ArrayList<>();
        this.player2Hexes = new ArrayList<>();

        int k = 3;
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= k; j++) {
                player1Hexes.add(grid[i][j]);
                isOccupied[i][j] = true;
            }
            k--;
        }

        k = 6;
        for (int i = 8; i >= 7; i--) {
            for (int j = k; j <= size; j++) {
                player2Hexes.add(grid[i][j]);
                isOccupied[i][j] = true;
            }
            k++;
        }
    }

    public int checkCellOwner(int x, int y) {
        if (isValidPosition(x, y)) {
            HexCell cell = grid[x][y];
            if (cell.isOccupied()) {
                if(cell.getOwner().equals(playerOne)) {
                    return 1;
                }
                return 0;
            } else {
                return -1;
            }
        }
        return "Invalid position";
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

    public boolean isValidPosition(int x, int y) {
        if(x <= 0 || y <= 0 || x > size || y > size) {
            return false;
        }
        return true;
    }

    public void resetBoard() {
        initializeBoard();
    }

    private void initializeBoard() {
        grid = new HexCell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new HexCell(i, j);
            }
        }
    }
}
