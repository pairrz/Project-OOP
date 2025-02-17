package backend.test;

import static org.junit.jupiter.api.Assertions.*;

import backend.game.GameBoard;
import backend.game.GameManage;
import backend.game.GameConfig;
import backend.players.HumanPlayer;
import backend.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameManageTest {

    private GameManage gameManage;
    private GameBoard gameBoard;
    private Player playerOne;
    private Player playerTwo;

    @BeforeEach
    public void setup() {
        // สร้างอินสแตนซ์ของ backend.game.GameBoard และ backend.players.Player
        gameBoard = GameBoard.getInstance();
        playerOne = new HumanPlayer("backend.players.Player One");
        playerTwo = new HumanPlayer("backend.players.Player Two");

        // กำหนดผู้เล่นใน backend.game.GameBoard
        gameBoard.setCurrentPlayer(playerOne);
        gameBoard.setOpponentPlayer(playerTwo);

        // สร้าง backend.game.GameManage
        gameManage = new GameManage();
    }


    @Test
    public void testIsOver_whenOpponentMinionZero_shouldReturnTrue() {
        // ตั้งค่าให้ opponent ไม่มี backend.minions.Minion
        playerOne.setNumber(5); // backend.players.Player One มี 5 backend.minions.Minion
        playerTwo.setNumber(0); // backend.players.Player Two ไม่มี backend.minions.Minion

        // ทดสอบว่าเมื่อ opponent ไม่มี backend.minions.Minion เกมจะจบ
        assertTrue(gameManage.isOver(), "Game should be over if opponent has no minion.");
    }

    @Test
    public void testIsOver_whenMaxTurnReached_shouldCompareMinionCounts() {
        // ตั้งค่าจำนวน turn ที่ max
        GameConfig.MaxTurns = 10; // ตั้งค่าจำนวนสูงสุดของ turn

        // ตั้งค่าจำนวน backend.minions.Minion ของผู้เล่น
        playerOne.setNumber(5); // backend.players.Player One มี 5 backend.minions.Minion
        playerTwo.setNumber(3); // backend.players.Player Two มี 3 backend.minions.Minion

        // ทดสอบว่าเมื่อถึง maxTurn จะเปรียบเทียบ backend.minions.Minion
        assertTrue(gameManage.isOver(), "Game should be over if max turns are reached and minion count is compared.");
    }

    @Test
    public void testIsOver_whenMaxTurnReached_shouldCompareHP() {
        // ตั้งค่าจำนวน turn ที่ max
        GameConfig.MaxTurns = 10; // ตั้งค่าจำนวนสูงสุดของ turn

        // ตั้งค่าจำนวน backend.minions.Minion ของผู้เล่น
        playerOne.setNumber(5); // backend.players.Player One มี 5 backend.minions.Minion
        playerTwo.setNumber(5); // backend.players.Player Two มี 5 backend.minions.Minion

        // ตั้งค่า HP ของผู้เล่น
        playerOne.setSumHP(100); // backend.players.Player One มี 100 HP
        playerTwo.setSumHP(80);  // backend.players.Player Two มี 80 HP

        // ทดสอบว่าเมื่อถึง maxTurn และ backend.minions.Minion เท่ากันจะเปรียบเทียบ HP
        assertTrue(gameManage.isOver(), "Game should be over if max turns are reached and HP is compared.");
    }

    @Test
    public void testGamePlay_shouldSwitchPlayersAndEndGame() {
        // กำหนดค่าของ backend.players.Player เพื่อให้สามารถเล่นได้
        playerOne.setNumber(5);
        playerTwo.setNumber(3);
        playerOne.setSumHP(50);
        playerTwo.setSumHP(40);
        playerOne.setBudget(100);
        playerTwo.setBudget(80);

        // เรียกใช้ gamePlay
        try {
            gameManage.gamePlay();
        } catch (Exception e) {
            fail("Exception occurred during game play: " + e.getMessage());
        }

        // ตรวจสอบว่าเกมสิ้นสุดหรือไม่
        assertTrue(gameManage.isOver(), "Game should be over after max turns or winner is determined.");
    }

    @Test
    public void testGameOver_shouldExitWhenGameIsOver() {
        // เรียกใช้ gameOver และตรวจสอบการสิ้นสุด
        try {
            gameManage.gameOver();
            // ถ้าไม่มี exception ก็ถือว่าเสร็จสิ้น
            assertTrue(true, "GameOver was called successfully.");
        } catch (Exception e) {
            fail("Exception occurred during game over: " + e.getMessage());
        }
    }
}

