package backend.game;

import backend.players.*;

import java.io.IOException;
import java.util.Scanner;

public class GameManage {
    private GameBoard board;
    public static int turn = 1;
    private int maxTurn;

    public void gamePlay() throws IOException {
        try {
            FileProcess file = new FileProcess();
            file.readConfig("D:\\OOP project\\Configuration");
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
        }

        maxTurn = GameConfig.MaxTurns;

        System.out.println("1.Duel Mode\n2.Solitaire Mode\n3.Auto Mode");
        System.out.print("Select game mode: ");

        Scanner scanner = new Scanner(System.in);
        int mode = scanner.nextInt();

        System.out.print("Enter the first player's name: ");
        String name1 = scanner.next();

        System.out.print("Enter the second player's name: ");
        String name2 = scanner.next();

//        System.out.print("Enter the strategy : ");
//        String strategy = scanner.next();

        this.board = GameBoard.getInstance(name1, name2);
        board.showBoard();

        while (true) {
            System.out.println(currentName() + "'s turn (Turn: " + turn + ")\n");
            current().takeTurn(turn);
            board.showBoard();

//            if (isOver()) {
//                System.out.println("Congratulations! " + currentName() + " won!");
//                break;
//            }

            board.switchPlayers();
            turn++;
        }

        //board.resetBoard();
        //gameOver();
    }

    private boolean isOver() {
        if (turn > maxTurn) {
            return determineWinner();
        }
        return (opponentMin() == 0 || currentMin() == 0) && turn > 10;
    }

    private boolean determineWinner() {
        if (currentMin() > opponentMin()) {
            System.out.println("Congratulations! " + currentName() + " won!");
            return true;
        } else if (currentMin() < opponentMin()) {
            System.out.println("Congratulations! " + opponentName() + " won!");
            return true;
        } else {
            if (currentHP() > opponentHP()) {
                System.out.println("Congratulations! " + currentName() + " won!");
                return true;
            } else if (currentHP() < opponentHP()) {
                System.out.println("Congratulations! " + opponentName() + " won!");
                return true;
            } else {
                if (currentBudget() > opponentBudget()) {
                    System.out.println("Congratulations! " + currentName() + " won!");
                } else {
                    System.out.println("Congratulations! " + opponentName() + " won!");
                }
                return true;
            }
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

    private String opponentName() {
        return board.getOpponentPlayer().getName();
    }

    private int currentMin() {
        return board.getCurrentPlayer().getNumber();
    }

    private int opponentMin() {
        return board.getOpponentPlayer().getNumber();
    }

    private int currentHP() {
        return board.getCurrentPlayer().getSumHP();
    }

    private int opponentHP() {
        return board.getOpponentPlayer().getSumHP();
    }

    private int currentBudget() {
        return board.getCurrentPlayer().getBudget();
    }

    private int opponentBudget() {
        return board.getOpponentPlayer().getBudget();
    }
}
