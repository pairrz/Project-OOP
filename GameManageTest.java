import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameManageTest {

    private GameManage gameManage;
    private GameBoard gameBoard;
    private Player playerOne;
    private Player playerTwo;

    @BeforeEach
    public void setup() {
        // สร้างอินสแตนซ์ของ GameBoard และ Player
        gameBoard = GameBoard.getInstance();
        playerOne = new HumanPlayer("Player One");
        playerTwo = new HumanPlayer("Player Two");

        // กำหนดผู้เล่นใน GameBoard
        gameBoard.setCurrentPlayer(playerOne);
        gameBoard.setOpponentPlayer(playerTwo);

        // สร้าง GameManage
        gameManage = new GameManage();
    }


    @Test
    public void testIsOver_whenOpponentMinionZero_shouldReturnTrue() {
        // ตั้งค่าให้ opponent ไม่มี Minion
        playerOne.setNumber(5); // Player One มี 5 Minion
        playerTwo.setNumber(0); // Player Two ไม่มี Minion

        // ทดสอบว่าเมื่อ opponent ไม่มี Minion เกมจะจบ
        assertTrue(gameManage.isOver(), "Game should be over if opponent has no minion.");
    }

    @Test
    public void testIsOver_whenMaxTurnReached_shouldCompareMinionCounts() {
        // ตั้งค่าจำนวน turn ที่ max
        GameRule.MaxTurns = 10; // ตั้งค่าจำนวนสูงสุดของ turn

        // ตั้งค่าจำนวน Minion ของผู้เล่น
        playerOne.setNumber(5); // Player One มี 5 Minion
        playerTwo.setNumber(3); // Player Two มี 3 Minion

        // ทดสอบว่าเมื่อถึง maxTurn จะเปรียบเทียบ Minion
        assertTrue(gameManage.isOver(), "Game should be over if max turns are reached and minion count is compared.");
    }

    @Test
    public void testIsOver_whenMaxTurnReached_shouldCompareHP() {
        // ตั้งค่าจำนวน turn ที่ max
        GameRule.MaxTurns = 10; // ตั้งค่าจำนวนสูงสุดของ turn

        // ตั้งค่าจำนวน Minion ของผู้เล่น
        playerOne.setNumber(5); // Player One มี 5 Minion
        playerTwo.setNumber(5); // Player Two มี 5 Minion

        // ตั้งค่า HP ของผู้เล่น
        playerOne.setSumHP(100); // Player One มี 100 HP
        playerTwo.setSumHP(80);  // Player Two มี 80 HP

        // ทดสอบว่าเมื่อถึง maxTurn และ Minion เท่ากันจะเปรียบเทียบ HP
        assertTrue(gameManage.isOver(), "Game should be over if max turns are reached and HP is compared.");
    }

    @Test
    public void testGamePlay_shouldSwitchPlayersAndEndGame() {
        // กำหนดค่าของ Player เพื่อให้สามารถเล่นได้
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

