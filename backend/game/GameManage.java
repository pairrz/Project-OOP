package backend.game;

import backend.players.*;

import java.io.IOException;
import java.util.Scanner;

public class GameManage {
    private GameBoard gameBoard;
    public static int turn = 1;
    private int maxTurn = GameConfig.MaxTurns;

    public void gamePlay() throws IOException {
        // อ่านค่าคอนฟิกจากไฟล์
        try {
            FileProcess file = new FileProcess();
            file.readConfig("D:\\OOP project\\Configuration");
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
        }

        System.out.println("1.Duel Mode\n2.Solitaire Mode\n3.Auto Mode");
        System.out.print("Select game mode : ");

        Scanner str1 = new Scanner(System.in);
        int x = str1.nextInt();

        switch (x){
            case 1:
            case 2:
            case 3:
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter the first player's name: ");
                String name1 = scanner.next();

                System.out.print("Enter the second player's name: ");
                String name2 = scanner.next();

                gameBoard = GameBoard.getInstance(name1, name2);

//                System.out.print("Enter the number of minion types: ");
//                int n = scanner.nextInt();
//
//                System.out.print("Enter your strategy: ");
//                String strategy = scanner.next();
//                scanner.close();

                //showBoard();
                break;
        }

        // เล่นเกมจนกว่าจะจบ
        while (!isOver()) {
            String winnerName = currentName(); // บันทึกชื่อก่อนสลับผู้เล่น
            System.out.println(currentName() + " turn");
            current().takeTurn(turn);
            gameBoard.setStatus();
            //showBoard();

            if (isOver()) {
                System.out.println("Congratulations! " + winnerName + " won!");
                break; // ออกจาก loop ทันทีหลังจากพบผู้ชนะ
            }

            gameBoard.switchPlayers();
            turn++;
        }
        // รีเซ็ตบอร์ดหลังจากจบเกม
        gameBoard.resetBoard();
        gameOver();
    }

    // ฟังก์ชันตรวจสอบว่าเกมจบหรือยัง
    private boolean isOver(){
        // ตรวจสอบว่า backend.minions.Minion ของผู้เล่นฝ่ายตรงข้ามหรือผู้เล่นฝ่ายปัจจุบันเป็น 0 หรือไม่
        if(opponentMin() == 0 && turn > 3){
            System.out.println("Congratulations! " + currentName() + " won!");
            return true;
        }else if(currentMin() == 0 && turn > 3){
            System.out.println("Congratulations! " + opponentName() + " won!");
            return true;
        }

        // ถ้า turn ถึง maxTurn จะเริ่มการเปรียบเทียบ
        if(turn > maxTurn){
            // เปรียบเทียบ backend.minions.Minion ของทั้งสองฝ่าย
            if(currentMin() > opponentMin()){
                System.out.println("Congratulations! " + currentName() + " won!");
                return true;
            } else if(currentMin() < opponentMin()){
                System.out.println("Congratulations! " + opponentName() + " won!");
                return true;
            } else {
                // ถ้า backend.minions.Minion เท่ากัน เปรียบเทียบ HP
                if(currentHP() > opponentHP()){
                    System.out.println("Congratulations! " + currentName() + " won!");
                    return true;
                } else if(currentHP() < opponentHP()){
                    System.out.println("Congratulations! " + opponentName() + " won!");
                    return true;
                } else {
                    // ถ้า HP เท่ากัน เปรียบเทียบ Budget
                    if(currentBudget() > opponentBudget()){
                        System.out.println("Congratulations! " + currentName() + " won!");
                        return true;
                    } else {
                        System.out.println("Congratulations! " + opponentName() + " won!");
                        return true;
                    }
                }
            }
        }else return false;// เกมยังไม่จบ
    }

    // จบเกมและรีเซ็ตสถานะ
    private void gameOver() {
        System.out.println("Game has ended.");
    }

    // ฟังก์ชันช่วยในการเข้าถึงผู้เล่นปัจจุบัน
    private Player current(){
        return gameBoard.getCurrentPlayer();
    }

    private String currentName() {
        return gameBoard.getCurrentPlayer().getName();
    }

    private String opponentName(){
        return gameBoard.getOpponentPlayer().getName();
    }

    // ฟังก์ชันช่วยในการเข้าถึงจำนวน backend.minions.Minion ของผู้เล่น
    private int currentMin(){
        return gameBoard.getCurrentPlayer().getNumber();
    }

    private int opponentMin(){
        return gameBoard.getOpponentPlayer().getNumber();
    }

    // ฟังก์ชันช่วยในการเข้าถึง HP ของผู้เล่น
    private int currentHP(){
        return gameBoard.getCurrentPlayer().getSumHP();
    }

    private int opponentHP(){
        return gameBoard.getOpponentPlayer().getSumHP();
    }

    // ฟังก์ชันช่วยในการเข้าถึง Budget ของผู้เล่น
    private int currentBudget(){
        return gameBoard.getCurrentPlayer().getBudget();
    }

    private int opponentBudget(){
        return gameBoard.getOpponentPlayer().getBudget();
    }
}
