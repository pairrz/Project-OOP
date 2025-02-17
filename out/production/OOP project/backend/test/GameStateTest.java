package backend.test;

import backend.game.GameBoard;
import backend.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    private GameBoard gameBoard;

    // ฟังก์ชัน setup สำหรับเตรียมข้อมูลก่อนการทดสอบ
    @BeforeEach
    void setUp() {
        gameBoard = GameBoard.getInstance();  // ใช้ Singleton pattern สำหรับสร้าง instance ของ backend.game.GameBoard
        gameBoard.setupPlayerHexes();  // ตั้งค่าผู้เล่น Hex

    }

    @Test
    void testPlayerHexesSetup() {
        GameBoard gameBoard = GameBoard.getInstance(); // Create an instance of backend.game.GameBoard
        gameBoard.setupPlayerHexes();  // Call setupPlayerHexes method

        // Check if player1Hexes contains the expected cells
        assertTrue(gameBoard.getPlayer1Hexes().contains(gameBoard.getGrid()[0][0]), "Expected player1Hexes to contain grid[0][0]");
        assertTrue(gameBoard.getPlayer1Hexes().contains(gameBoard.getGrid()[0][1]), "Expected player1Hexes to contain grid[0][1]");
        assertTrue(gameBoard.getPlayer1Hexes().contains(gameBoard.getGrid()[1][0]), "Expected player1Hexes to contain grid[1][0]");

        // Check if player2Hexes contains the expected cells
        assertTrue(gameBoard.getPlayer2Hexes().contains(gameBoard.getGrid()[7][6]), "Expected player2Hexes to contain grid[7][6]");
        assertTrue(gameBoard.getPlayer2Hexes().contains(gameBoard.getGrid()[7][7]), "Expected player2Hexes to contain grid[7][7]");
    }

    // ทดสอบการสลับผู้เล่น
    @Test
    void testSwitchPlayers() {
        Player currentPlayer = gameBoard.getCurrentPlayer();
        Player opponentPlayer = gameBoard.getOpponentPlayer();

        // สลับผู้เล่น
        gameBoard.switchPlayers();

        // ตรวจสอบว่า switchPlayers() ทำงานอย่างถูกต้อง
        assertNotEquals(currentPlayer, gameBoard.getCurrentPlayer(), "Current player should be switched.");
        assertNotEquals(opponentPlayer, gameBoard.getOpponentPlayer(), "Opponent player should be switched.");
    }


    // ทดสอบการตรวจสอบเจ้าของเซลล์
    @Test
    void testCheckCellOwner() {
        // กำหนดให้เซลล์ [0][0] ถูกยึดครองโดยผู้เล่น 1
        gameBoard.getGrid()[0][0].setOwner(gameBoard.getCurrentPlayer());
        gameBoard.getGrid()[0][0].setOccupied(true);

        // ตรวจสอบว่าเซลล์ [0][0] เป็นของผู้เล่น 1
        assertEquals(1, gameBoard.checkCellOwner(0, 0), "Expected cell [0][0] to be owned by backend.players.Player 1");

        // เปลี่ยนเจ้าของเซลล์ [0][0] เป็นผู้เล่น 2
        gameBoard.getGrid()[0][0].setOwner(gameBoard.getOpponentPlayer());

        // ตรวจสอบว่าเซลล์ [0][0] ตอนนี้เป็นของผู้เล่น 2
        assertEquals(0, gameBoard.checkCellOwner(0, 0), "Expected cell [0][0] to be owned by backend.players.Player 2");
    }


    // ทดสอบการตรวจสอบเซลล์ว่าถูกยึดครองหรือไม่
    @Test
    void testIsOccupied() {
        // ตรวจสอบเซลล์ที่ไม่ได้ถูกยึดครอง
        assertFalse(gameBoard.isOccupied(0, 0), "Expected cell [0][0] to not be occupied initially.");

        // ตั้งค่าการยึดครองเซลล์
        gameBoard.getGrid()[0][0].setOccupied(true);

        // ตรวจสอบว่าเซลล์ถูกยึดครองแล้ว
        assertTrue(gameBoard.isOccupied(0, 0), "Expected cell [0][0] to be occupied after setting it as occupied.");
    }

    // ทดสอบฟังก์ชัน isValidPosition
    @Test
    void testIsValidPosition() {
        // ตรวจสอบตำแหน่งที่อยู่ในขอบเขตของกริด
        assertTrue(gameBoard.isValidPosition(0, 0), "Expected position [0][0] to be valid.");
        assertTrue(gameBoard.isValidPosition(7, 7), "Expected position [7][7] to be valid.");

        // ตรวจสอบตำแหน่งที่อยู่นอกขอบเขต
        assertFalse(gameBoard.isValidPosition(-1, -1), "Expected position [-1][-1] to be invalid.");
        assertFalse(gameBoard.isValidPosition(8, 8), "Expected position [8][8] to be invalid.");
    }

    // ทดสอบการรีเซ็ตกระดาน
    @Test
    void testResetBoard() {
        // ตั้งค่าผู้เล่น Hex
        gameBoard.setupPlayerHexes();

        // ตรวจสอบว่าผู้เล่น 1 มี Hexes ที่คาดไว้
        assertTrue(gameBoard.getPlayer1Hexes().contains(gameBoard.getGrid()[0][0]), "Expected player1Hexes to contain grid[0][0]");

        // รีเซ็ตกระดาน
        gameBoard.resetBoard();

        // ตรวจสอบว่ากระดานถูกรีเซ็ตแล้ว
        assertFalse(gameBoard.getPlayer1Hexes().contains(gameBoard.getGrid()[0][0]), "Expected player1Hexes to not contain grid[0][0] after reset.");
    }
}

