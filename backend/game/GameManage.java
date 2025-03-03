package backend.game;

import backend.players.*;
import java.io.IOException;
import java.util.Scanner;

public class GameManage {
    private GameBoard board;
    public static int turn = 1;
    private int maxTurn;

    public void gamePlay() throws IOException {
        FileProcess file = new FileProcess();
        file.readConfig("D:\\OOP project\\backend\\Configuration");

        maxTurn = GameConfig.MaxTurns;

        System.out.println("-----Select game mode-----\n1.Duel Mode\n2.Solitaire Mode\n3.Auto Mode");
        System.out.print("your answer : ");

        Scanner scanner = new Scanner(System.in);
        int mode = scanner.nextInt();

        switch (mode) {
            case 1:
                String name1 = askName();
                String name2 = askName();
                this.board = GameBoard.getInstance(name1, name2);
                break;
            case 2:
                String name3 = askName();
                this.board = GameBoard.getInstance(name3);
                break;
            case 3:
                this.board = GameBoard.getInstance();
                break;
            default:
                break;
        }

//        System.out.print("Enter the strategy : ");
//        String strategy = scanner.next();

        board.showBoard();

        while (!isOver()) {
            System.out.println("\n" + currentName() + "'s turn (Turn: " + turn + ")\n");
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
        if (turn > 10 && (opponentMin() == 0 || currentMin() == 0)) {
            System.out.println("Game over! A player has no minions left in the territory.");
            determineWinner();
            return true;
        }

        if (turn >= maxTurn) {
            System.out.println("Game over! Reached the maximum number of turns.");
            determineWinner();
            return true;
        }

        return false;
    }

    private void determineWinner() {
        int currentMinions = currentMin();
        int opponentMinions = opponentMin();

        // ผู้เล่นที่มีมินเนียนมากกว่าชนะ
        if (currentMinions > opponentMinions) {
            System.out.println("Congratulations! " + currentName() + " won!");
            return;
        } else if (currentMinions < opponentMinions) {
            System.out.println("Congratulations! " + opponentName() + " won!");
            return;
        }

        // ถ้ามีจำนวนมินเนียนเท่ากัน ให้ดูที่ HP รวมของมินเนียน
        int currentTotalHP = currentHP();
        int opponentTotalHP = opponentHP();

        if (currentTotalHP > opponentTotalHP) {
            System.out.println("Congratulations! " + currentName() + " won!");
            return;
        } else if (currentTotalHP < opponentTotalHP) {
            System.out.println("Congratulations! " + opponentName() + " won!");
            return;
        }

        // ถ้า HP รวมเท่ากัน ให้ดูที่งบประมาณที่เหลืออยู่
        int currentRemainingBudget = currentBudget();
        int opponentRemainingBudget = opponentBudget();

        if (currentRemainingBudget > opponentRemainingBudget) {
            System.out.println("Congratulations! " + currentName() + " won!");
        } else if (currentRemainingBudget < opponentRemainingBudget) {
            System.out.println("Congratulations! " + opponentName() + " won!");
        } else {
            System.out.println("The game is a tie!");
        }
    }

    private String askName(){
        System.out.print("Enter the player's name: ");
        Scanner scanner = new Scanner(System.in);

        return scanner.next();
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
        return board.getCurrentPlayer().getNumMinions();
    }

    private int opponentMin() {
        return board.getOpponentPlayer().getNumMinions();
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
