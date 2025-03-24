package backend.game;

import backend.players.*;
import java.util.HashMap;
import java.util.Map;

public class GameBoard {
    private final int size = 8;

    private static GameBoard instance;
    public static Map<String, HexCell> hexCellMap;
    public static boolean HaveStrategy;
    public static String Strategy;
    public static boolean[][] isOccupied;

    private static Player playerOne;
    private static Player playerTwo;
    private Player currentPlayer;
    private Player opponentPlayer;

    private final Map<String, HexCell> player1Hexes;
    private final Map<String, HexCell> player2Hexes;

    private static final int spawnRemaining = GameConfig.MaxSpawns;

    //main constructor
    private GameBoard(String playerOneName, String playerTwoName,boolean isBotOne,boolean isBotTwo) {
        hexCellMap = new HashMap<>();
        this.player1Hexes = new HashMap<>();
        this.player2Hexes = new HashMap<>();
        isOccupied = new boolean[size][size];

        initializePlayers(playerOneName, playerTwoName,isBotOne,isBotTwo);

        currentPlayer = playerOne;
        opponentPlayer = playerTwo;

        setBoard();
        setupPlayerOneHexes();
        setupPlayerTwoHexes();
    }

    //player va bot
    private GameBoard(String playerOneName) {
        this(playerOneName, "Bot",false,true);
    }

    //bot vs bot
    private GameBoard() {
        this("Bot1", "Bot2",true,true);
    }

    private void initializePlayers(String playerOneName, String playerTwoName,boolean isBotOne, boolean isBotTwo) {
        playerOne = isBotOne ? new BotPlayer(playerOneName, player1Hexes) : new Player(playerOneName, player1Hexes);
        playerTwo = isBotTwo ? new BotPlayer(playerTwoName, player2Hexes) : new Player(playerTwoName, player2Hexes);
    }

    //singleton
    public static GameBoard getInstance(String playerOneName, String playerTwoName) {
        if (instance == null) {
            instance = new GameBoard(playerOneName, playerTwoName,false, false);
        }
        return instance;
    }

    public static GameBoard getInstance(String playerOneName) {
        if (instance == null) {
            instance = new GameBoard(playerOneName);
        }
        return instance;
    }

    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public static HexCell getHexCell(int x, int y) {
        String key = x + "," + y;
        return hexCellMap.computeIfAbsent(key, k -> new HexCell(x,y));
    }

    private void setBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                getHexCell(i, j);
                isOccupied[i][j] = false;
            }
        }
    }

    public void setupPlayerOneHexes() {
        int k = 3;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < k; j++) {
                HexCell cell = getHexCell(i, j);
                player1Hexes.put(i + "," + j, cell);
                cell.setOwner(playerOne);
                cell.setStatus("1");
                isOccupied[i][j] = true;
            }
            k--;
        }
    }

    public void setupPlayerTwoHexes() {
        int k = 5;
        for (int i = 7; i >= 6; i--) {
            for (int j = k; j < size; j++) {
                HexCell cell = getHexCell(i, j);
                player2Hexes.put(i + "," + j, cell);
                cell.setOwner(playerTwo);
                cell.setStatus("2");
                isOccupied[i][j] = true;
            }
            k++;
        }
    }

    public String getStatusHexCells(int x, int y) {
        String key = x + "," + y;
        if (hexCellMap.containsKey(key)) {
            return hexCellMap.get(key).getStatus();
        }
        return "-";
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayers() {
        Player temp = currentPlayer;
        currentPlayer = opponentPlayer;
        opponentPlayer = temp;
    }

    public static int getSpawnRemaining() {
        return spawnRemaining - (playerOne.getNumMinions() + playerTwo.getNumMinions());
    }

    public static boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }

    public void resetBoard() {
        hexCellMap.clear();
    }

    public void setStatus() {
        for (HexCell cell : hexCellMap.values()) {
            if (cell.hasMinion()) {
                if (cell.getMinion().getOwner() == playerOne) {
                    cell.setStatus("*");
                } else {
                    cell.setStatus("#");
                }
            } else {
                if(cell.getOwner() == playerOne){
                    cell.setStatus("1");
                }else if(cell.getOwner() == playerTwo){
                    cell.setStatus("2");
                }else {
                    cell.setStatus("-");
                }
            }
        }
    }

    public static void setOccupied(int x , int y){
        isOccupied[x][y] = true;
    }

    public static boolean isOccupied(int x, int y) {
        return isOccupied[x][y];
    }

    public void showBoard() {
        setStatus();

        System.out.println();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print( getStatusHexCells(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    /*
    for tester
     */

    public void buyHexForPlayerOne(HexCell cell) { //forT
        playerOne.buyHexCell(cell);
        setStatus();
    }

    public void buyHexForPlayerTwo(HexCell cell) {
        playerTwo.buyHexCell(cell);
        setStatus();
    }

    public void buyMinionForPlayerOne(HexCell cell) {
        playerOne.buyMinion(cell);
        setStatus();
    }

    public void buyMinionForPlayerTwo(HexCell cell) {
        playerTwo.buyMinion(cell);
        setStatus();
    }
}
