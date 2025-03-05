package backend.game;

import backend.players.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameBoard {
    private static GameBoard instance;
    private static Map<String, HexCell> hexCellMap;
    private static String namePlayerOne, namePlayerTwo;

    private final int size = 8;
    private final boolean[][] isOccupied = new boolean[size][size];

    private static Player playerOne;
    private static Player playerTwo;
    private Player currentPlayer;
    private Player opponentPlayer;

    private final Map<String, HexCell> player1Hexes;
    private final Map<String, HexCell> player2Hexes;

    private static final int spawnRemaining = GameConfig.MaxSpawns;

    //main constructor
    private GameBoard(String playerOneName, String playerTwoName,boolean isBotOne,boolean isBotTwo) throws IOException {
        hexCellMap = new HashMap<>();
        player1Hexes = new HashMap<>();
        player2Hexes = new HashMap<>();

        initializePlayers(playerOneName, playerTwoName,isBotOne,isBotTwo);

        namePlayerOne = playerOneName;
        namePlayerTwo = playerTwoName;

        currentPlayer = playerOne;
        opponentPlayer = playerTwo;

        setBoard();
        setupPlayerOneHexes();
        setupPlayerTwoHexes();
    }

    //player va bot
    private GameBoard(String playerOneName) throws IOException {
        this(playerOneName, "Bot",false,true);
    }

    //bot vs bot
    private GameBoard() throws IOException {
        this("Bot1", "Bot2",true,true);
    }

    private void initializePlayers(String playerOneName, String playerTwoName,boolean isBotOne, boolean isBotTwo) throws IOException {
        playerOne = isBotOne ? new BotPlayer(playerOneName, setupPlayerOneHexes()) : new Player(playerOneName, setupPlayerOneHexes());
        playerTwo = isBotTwo ? new BotPlayer(playerTwoName, setupPlayerTwoHexes()) : new Player(playerTwoName, setupPlayerTwoHexes());
    }

    //singleton
    public static GameBoard getInstance(String playerOneName, String playerTwoName) throws IOException {
        if (instance == null) {
            instance = new GameBoard(playerOneName, playerTwoName,false, false);
        }
        return instance;
    }

    public static GameBoard getInstance(String playerOneName) throws IOException {
        if (instance == null) {
            instance = new GameBoard(playerOneName);
        }
        return instance;
    }

    public static GameBoard getInstance() throws IOException {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public static HexCell getHexCell(int x, int y) {
        String key = x + "," + y;
        return hexCellMap.computeIfAbsent(key, k -> new HexCell(x, y));
    }

    private void setBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                getHexCell(i, j);
//                isOccupied[i][j] = false;
            }
        }
    }

    //Setup player hexes
//    private void setupPlayerHexes() {
//        int k = 3;
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < k; j++) {
//                HexCell cell = getHexCell(i, j);
//                player1Hexes.put(i + "," + j, cell);
//                cell.setOwner(playerOne);
//                cell.setStatus("1");
//                //isOccupied[i][j] = true;
//            }
//            k--;
//        }
//
//        k = 5;
//        for (int i = 7; i >= 6; i--) {
//            for (int j = k; j < size; j++) {
//                HexCell cell = getHexCell(i, j);
//                player2Hexes.put(i + "," + j, cell);
//                cell.setOwner(playerTwo);
//                cell.setStatus("2");
//                //isOccupied[i][j] = true;
//            }
//            k++;
//        }
//    }

    public Map<String, HexCell> setupPlayerOneHexes() throws IOException {
        int k = 3;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < k; j++) {
                HexCell cell = getHexCell(i, j);
                player1Hexes.put(i + "," + j, cell);
                cell.setOwner(playerOne);
                cell.setStatus("1");
                //isOccupied[i][j] = true;
            }
            k--;
        }
        return player1Hexes;
    }

    public Map<String, HexCell> setupPlayerTwoHexes() throws IOException {
        int k = 5;
        for (int i = 7; i >= 6; i--) {
            for (int j = k; j < size; j++) {
                HexCell cell = getHexCell(i, j);
                player2Hexes.put(i + "," + j, cell);
                cell.setOwner(playerTwo);
                cell.setStatus("2");
                //isOccupied[i][j] = true;
            }
            k++;
        }
        return player2Hexes;
    }

    public Map<String, HexCell> getPlayer1Hexes() {
        return player1Hexes;
    }

    public Map<String, HexCell> getPlayer2Hexes() {
        return player2Hexes;
    }

    public String getStatusHexCells(int x, int y) {
        String key = x + "," + y; //
        if (hexCellMap.containsKey(key)) {
            return hexCellMap.get(key).getStatus();
        }
        return "-";
    }

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
        String key = x + "," + y;
        HexCell cell = hexCellMap.get(key);
        if (cell != null && cell.isOccupied()) {
            if (cell.getOwner().equals(playerOne)) {
                return 1; // Player 1
            }
            return 2; // Player 2
        } else {
            return 0; // No owner
        }
    }

    public static int getSpawnRemaining() {
        return spawnRemaining - (playerOne.getNumMinions() + playerTwo.getNumMinions());
    }

    public boolean isOccupied(int x, int y) {
        String key = x + "," + y;
        HexCell cell = hexCellMap.get(key);
        return cell != null && cell.isOccupied();
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

    public void showBoard() {
        setStatus();  //อัปเดตสถานะก่อนแสดงบอร์ด
        System.out.println();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print( getStatusHexCells(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void buyHexForPlayerOne(HexCell cell) {
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

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Map<String, HexCell> getHexCellMap() {
        return hexCellMap;
    }



    public void showHexOne(){
        System.out.println("one is");
        for (HexCell cell : player1Hexes.values()) {
            System.out.println(cell.getX() + "," + cell.getY());
        }
        System.out.println();
    }
    public void showHexTwo(){
        System.out.println("Two is");
        for (HexCell cell : player2Hexes.values()) {
            System.out.println(cell.getX() + "," + cell.getY());
        }
        System.out.println();
    }
}
