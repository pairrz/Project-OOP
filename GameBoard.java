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
        opponentPlayer = playerTwo;

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
        }else{
            currentPlayer = playerOne;
            opponentPlayer = playerTwo;
        }
    }

    public int checkCellOwner(int x, int y) {
        HexCell cell = grid[x][y];
        if (cell.isOccupied()) {
            if(cell.getOwner().equals(playerOne)) {
                return 1; //ผู้เล่นหนึ่ง
            }
            return 0; //ผู้เล่นสอง
        } else {
            return -1; //ไม่มีเจ้าของ
        }
    }

//    public ArrayList<HexCell> getPlayer1Hexes() {
//        return player1Hexes;
//    }
//
//    public ArrayList<HexCell> getPlayer2Hexes() {
//        return player2Hexes;
//    }

    public int spawnleft(){
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
        return x > 0 && y > 0 && x <= size && y <= size;
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
