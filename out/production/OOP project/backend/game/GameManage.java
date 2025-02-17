package backend.game;

import backend.players.Player;

import java.io.IOException;

public class GameManage {
    private GameBoard gameBoard;
    public static int turn = 1;
    private int maxTurn = GameRule.MaxTurns;

    public GameManage(){
        gameBoard = GameBoard.getInstance();
    }

    public void gamePlay() throws IOException {
        // อ่านค่าคอนฟิกจากไฟล์
        FileProcess file = new FileProcess();
        file.readConfig("D:\\OOP project\\Configuration");

        // เล่นเกมจนกว่าจะจบ
        while(!isOver()){
            current().takeTurn(turn);
            gameBoard.switchPlayers();
            turn++;
        }
        // รีเซ็ตบอร์ดหลังจากจบเกม
        gameBoard.resetBoard();
        gameOver();
    }

    // ฟังก์ชันตรวจสอบว่าเกมจบหรือยัง
    boolean isOver(){
        // ตรวจสอบว่า backend.minions.Minion ของผู้เล่นฝ่ายตรงข้ามหรือผู้เล่นฝ่ายปัจจุบันเป็น 0 หรือไม่
        if(opponentMin() == 0 && turn > 2){
            System.out.println("Congratulations! " + currentName() + " won!");
            return true;
        }else if(currentMin() == 0 && turn > 2){
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
        }

        return false; // เกมยังไม่จบ
    }

    // จบเกมและรีเซ็ตสถานะ
    void gameOver() {
        System.out.println("Game has ended.");
    }

    // ฟังก์ชันช่วยในการเข้าถึงผู้เล่นปัจจุบัน
    private Player current(){
        return gameBoard.getCurrentPlayer();
    }

    private String currentName(){
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
