package backend.game;

import backend.players.*;
import java.util.ArrayList;

public class GameBoard {
    public static GameBoard instance;
    private HexCell[][] grid;
    private int size = 8;
    private boolean[][] isOccupied = new boolean[8][8];

    private final Player playerOne;
    private final Player playerTwo;
    private Player currentPlayer;
    private Player opponentPlayer;
    private ArrayList<HexCell> player1Hexes;
    private ArrayList<HexCell> player2Hexes;
    private int SpawnRemaining = GameConfig.MaxSpawns;

    // Singleton pattern for backend.game.GameBoard
    public static GameBoard getInstance(String playerOneName, String playerTwoName) {
        if (instance == null) {
            instance = new GameBoard(playerOneName, playerTwoName);
        }
        return instance;
    }

    public static GameBoard getInstance() {
        return instance;
    }

    // Constructor
    public GameBoard(String playerOneName, String playerTwoName) {
        playerOne = new HumanPlayer(playerOneName);  // ใช้ชื่อที่รับมา
        playerTwo = new HumanPlayer(playerTwoName);  // ใช้ชื่อที่รับมา
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

        // Set up hexes for backend.players.Player 1
        int k = 3;
        for (int i = 0; i < 2; i++) {  // Use i = 0 to 1 instead of 1 to 2
            for (int j = 0; j < k; j++) {  // Use j = 0 to k-1 instead of 1 to k
                player1Hexes.add(grid[i][j]);
                grid[i][j].setStatus("1");
                grid[i][j].setOwner(playerOne);
                isOccupied[i][j] = true;
            }
            k--;
        }

        // Set up hexes for backend.players.Player 2
        k = 5;
        for (int i = 7; i >= 6; i--) {  // ใช้ i = 7 และ i = 6
            for (int j = k; j < size; j++) {  // ใช้ j = k ถึง j < size
                player2Hexes.add(grid[i][j]);
                grid[i][j].setOwner(playerTwo);
                grid[i][j].setStatus("2");
                isOccupied[i][j] = true;
            }
            k++;  // เพิ่มค่า k เพื่อเลื่อนขอบเขตของ j
        }
    }

    // Getter methods for player hexes and grid
    public ArrayList<HexCell> getPlayer1Hexes() {
        return player1Hexes;
    }

    public ArrayList<HexCell> getPlayer2Hexes() {
        return player2Hexes;
    }

    public String getStatusHexCells(int i, int j) {
        if (grid == null) {
            throw new IllegalStateException("Grid is not initialized!");
        }

        if (i < 0 || i > 8 || j < 0 || j > 8) {
            throw new IndexOutOfBoundsException("Invalid index: (" + i + ", " + j + ")");
        }
        return grid[i][j].getStatus();
    }

    // Other methods for game logic...
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getCurrentPlayerName() {
        return currentPlayer.getName();
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public String getOpponentPlayerName() {
        return opponentPlayer.getName();
    }

    public void switchPlayers() {
        Player temp = currentPlayer;
        currentPlayer = opponentPlayer;
        opponentPlayer = temp;
    }

    public int checkCellOwner(int x, int y) {
        HexCell cell = grid[x][y];
        if (cell.isOccupied()) {
            if(cell.getOwner().equals(playerOne)) {
                return 1; // backend.players.Player 1
            }
            return 0; // backend.players.Player 2
        } else {
            return -1; // No owner
        }
    }

    public int getSpawnRemaining() {
        return GameConfig.MaxSpawns - (playerOne.getNumber() + playerTwo.getNumber());
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

    /*
        - hex cell ว่าง
        1 ช่องของ py1
        2 ช่องของ py2
        * ช่องของมินเนียน py1
        # ช่องของมินเนียน py2
     */

    public void setStatus() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                HexCell cell = grid[i][j];
                if(cell.getMinion() == null && cell.getOwner() == null) {
                    cell.setStatus("-");
                }else if(cell.getMinion() == null && cell.getOwner() != null) {
                    if(cell.getOwner().equals(playerOne)) {
                        cell.setStatus("1");
                    }else{
                        cell.setStatus("2");
                    }
                }else{
                    if(cell.getOwner().equals(playerOne)) {
                        cell.setStatus("*");
                    }else{
                        cell.setStatus("#");
                    }
                }
            }
        }
    }

    public void showBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                System.out.print(getStatusHexCells(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void setOpponentPlayer(Player playerTwo) {
    }

    public void setCurrentPlayer(Player playerOne) {
    }

    public void onebymn(HexCell cell) {
        playerOne.buyMinion(cell);
    }

    public void secbymin(HexCell cell) {
        playerTwo.buyMinion(cell);
    }
}
