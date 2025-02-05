import java.util.ArrayList;

public class GameBoard {
    public static GameBoard instance;
    private HexCell[][] grid;
    private int size = 8;
    private boolean[][] isOccupied = new boolean[8][8];

    private Player playerOne, playerTwo;
    private Player currentPlayer;
    private Player opponentPlayer;
    private ArrayList<HexCell> player1Hexes;
    private ArrayList<HexCell> player2Hexes;
    private int spawnleft = GameRule.MaxSpawns;

    // Singleton pattern for GameBoard
    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    // Constructor
    public GameBoard() {
        playerOne = new HumanPlayer("Player One");
        playerTwo = new HumanPlayer("Player Two");
        currentPlayer = playerOne;
        opponentPlayer = playerTwo;

        grid = new HexCell[size][size];

        // Initialize the grid
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new HexCell(i, j);
                isOccupied[i][j] = false;
            }
        }
        setupPlayerHexes();
    }

    // Public method to setup player hexes
    public void setupPlayerHexes() {
        this.player1Hexes = new ArrayList<>();
        this.player2Hexes = new ArrayList<>();

        // Set up hexes for Player 1
        int k = 3;
        for (int i = 0; i < 2; i++) {  // Use i = 0 to 1 instead of 1 to 2
            for (int j = 0; j < k; j++) {  // Use j = 0 to k-1 instead of 1 to k
                player1Hexes.add(grid[i][j]);
                isOccupied[i][j] = true;
            }
            k--;
        }

        // Set up hexes for Player 2
        k = 6;
        for (int i = 7; i < 8; i++) {  // Use i = 7 to 7
            for (int j = k; j < size; j++) {  // Use j = k to size-1
                player2Hexes.add(grid[i][j]);
                isOccupied[i][j] = true;
            }
            k++;
        }
    }

    // Getter methods for player hexes and grid
    public ArrayList<HexCell> getPlayer1Hexes() {
        return player1Hexes;
    }

    public ArrayList<HexCell> getPlayer2Hexes() {
        return player2Hexes;
    }

    public HexCell[][] getGrid() {
        return grid;
    }

    // Other methods for game logic...
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public void switchPlayers() {
        if(currentPlayer == playerOne) {
            currentPlayer = playerTwo;
            opponentPlayer = playerOne;
        } else {
            currentPlayer = playerOne;
            opponentPlayer = playerTwo;
        }
    }

    public int checkCellOwner(int x, int y) {
        HexCell cell = grid[x][y];
        if (cell.isOccupied()) {
            if(cell.getOwner().equals(playerOne)) {
                return 1; // Player 1
            }
            return 0; // Player 2
        } else {
            return -1; // No owner
        }
    }

    public int spawnleft() {
        return spawnleft -= playerOne.getNumber() + playerTwo.getNumber();
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
        return x >= 0 && y >= 0 && x < size && y < size;
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

    public void setOpponentPlayer(Player playerTwo) {
    }

    public void setCurrentPlayer(Player playerOne) {
    }
}
