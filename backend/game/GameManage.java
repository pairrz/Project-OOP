package backend.game;

import backend.minions.Minion;
import backend.players.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManage {
    public static String[] selectedMinions;
    private GameBoard board;
    public static int turn = 1;
    private int maxTurn;

    public void gamePlay() throws Exception {
        FileProcess file = new FileProcess();
        file.readConfig("D:\\OOP project\\backend\\Configuration");

        maxTurn = GameConfig.MaxTurns;

        System.out.println("-----Select game mode-----\n1.Duel Mode\n2.Solitaire Mode\n3.Auto Mode");
        System.out.print("your answer : ");

        Scanner scanner = new Scanner(System.in);
        int mode = scanner.nextInt();

        switch (mode) {
            case 1:
                String name1 = setName();
                String name2 = setName();
                this.board = GameBoard.getInstance(name1, name2);
                break;
            case 2:
                String name3 = setName();
                this.board = GameBoard.getInstance(name3);
                break;
            case 3:
                this.board = GameBoard.getInstance();
                break;
            default:
                break;
        }
        chooseMinion();
        setStrategy();
        board.showBoard();

        while (!isOver()) {
            System.out.println("\n" + currentName() + "'s turn (Turn: " + turn + ")");
            current().takeTurn(turn);
            board.showBoard();

            System.out.println("-----End " + currentName() + "'s turn-----");
            board.switchPlayers();

            System.out.println("\n" + currentName() + "'s turn (Turn: " + turn + ")");
            current().takeTurn(turn);
            board.showBoard();
            System.out.println("-----End " + currentName() + "'s turn-----");

            board.switchPlayers();
            turn++;
        }
        board.resetBoard();
        gameOver();
    }

    private boolean isOver() {
        if (turn > 10 && (playerOneMin() == 0 || playerTwoMin() == 0)) {
            System.out.println("Game over! A player has no minions left in the territory.");
            determineWinner();
            showStatusPlayer();
            return true;
        }

        if (turn > maxTurn) {
            System.out.println("Game over! Reached the maximum number of turns.");
            determineWinner();
            showStatusPlayer();
            return true;
        }

        return false;
    }

    private void showStatusPlayer() {
        System.out.println("Player 1 " + playerOne().getName() + "  |  Player 2 " + playerTwo().getName());
        System.out.println("budgets: " + playerOne().getBudget() + "  |  budgets: " + playerTwo().getBudget());
        System.out.println("minions: " + playerOne().getMinions().size() + "  |  minions: " + playerTwo().getMinions().size());
        System.out.println("sum HP: " + playerOne().getSumHP() + "  |  sum HP: " + playerTwo().getSumHP());
    }

    private void determineWinner() {
        int currentMinions = playerOneMin();
        int opponentMinions = playerTwoMin();

        //check num of minions
        if (currentMinions > opponentMinions) {
            System.out.println("Congratulations! " + playerOneName() + " won!");
            return;
        } else if (currentMinions < opponentMinions) {
            System.out.println("Congratulations! " + playerTwoName() + " won!");
            return;
        }

        //check sum hp of minions
        int currentTotalHP = playerOneHP();
        int opponentTotalHP = playerTwoHP();

        if (currentTotalHP > opponentTotalHP) {
            System.out.println("Congratulations! " + playerOneName() + " won!");
            return;
        } else if (currentTotalHP < opponentTotalHP) {
            System.out.println("Congratulations! " + playerTwoName() + " won!");
            return;
        }

        //check budget
        int currentRemainingBudget = playerOneBudget();
        int opponentRemainingBudget = playerTwoBudget();

        if (currentRemainingBudget > opponentRemainingBudget) {
            System.out.println("Congratulations! " + playerOneName() + " won!");
        } else if (currentRemainingBudget < opponentRemainingBudget) {
            System.out.println("Congratulations! " + playerTwoName() + " won!");
        } else {
            System.out.println("The game is a tie!");
        }
    }

    private String setName(){
        System.out.print("Enter the player's name: ");
        Scanner scanner = new Scanner(System.in);

        return scanner.next();
    }

    private void setStrategy() {
        System.out.print("Do you want to write your strategy? (Y/N): ");
        Scanner scanner = new Scanner(System.in);
        String ans = scanner.next();

        if(ans.equalsIgnoreCase("Y")) {
            System.out.println("Enter your strategy (type 'END' on a new line to finish): ");
            scanner = new Scanner(System.in);
            StringBuilder strategy = new StringBuilder();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("END")) {
                    break;
                }
                strategy.append(line).append(" ");
            }
            GameBoard.Strategy = strategy.toString();
            GameBoard.HaveStrategy = true;
        }else if(ans.equalsIgnoreCase("N")) {
            GameBoard.HaveStrategy = false;
            GameBoard.Strategy = "D:\\OOP project\\backend\\strategy\\Strategy3.txt";
        }else {
            System.out.println("Invalid strategy");
        }
    }

    private void chooseMinion() {
        System.out.println("1. LordMinion");
        System.out.println("2. GiantMinion");
        System.out.println("3. WarriorMinion");
        System.out.println("4. HumanMinion");
        System.out.println("5. SlaveMinion");
        System.out.println("Choose minion types (e.g. 2,4,5): ");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // ตัดช่องว่างและตรวจสอบค่าที่ถูกต้อง
        String[] rawMinions = input.split(",");
        List<String> validMinions = new ArrayList<>();

        for (String minion : rawMinions) {
            minion = minion.trim();  // ลบช่องว่าง
            if (minion.matches("[1-5]")) { // ตรวจสอบว่าต้องเป็น 1-5 เท่านั้น
                validMinions.add(minion);
            }
        }

        // แปลงเป็น array และเก็บใน selectedMinions
        GameManage.selectedMinions = validMinions.toArray(new String[0]);

        if (GameManage.selectedMinions.length == 0) {
            System.out.println("Error: No valid minion types selected!");
        }
    }

    private void gameOver() {
        System.out.println("Game has ended.");
    }

    private Player current() {
        return board.getCurrentPlayer();
    }

    private String currentName() {
        return board.getCurrentPlayer().getName();
    }

    private String playerOneName() {
        return board.getPlayerOne().getName();
    }

    private String playerTwoName() {
        return board.getPlayerTwo().getName();
    }

    private int playerOneMin() {
        return board.getPlayerOne().getNumMinions();
    }

    private int playerTwoMin() {
        return board.getPlayerTwo().getNumMinions();
    }

    private int playerOneHP() {
        return board.getPlayerOne().getSumHP();
    }

    private int playerTwoHP() {
        return board.getPlayerTwo().getSumHP();
    }

    private int playerOneBudget() {
        return board.getPlayerOne().getBudget();
    }

    private int playerTwoBudget() {
        return board.getPlayerTwo().getBudget();
    }

    private Player playerOne(){
        return board.getPlayerOne();
    }

    private Player playerTwo(){
        return board.getPlayerTwo();
    }
}
