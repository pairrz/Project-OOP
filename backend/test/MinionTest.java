package backend.test;

import backend.game.*;
import backend.minions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class MinionTest {
    private GameBoard board;
    private Minion minion;
    private HexCell cell;

    @BeforeEach
    void setUp() throws IOException {
        board = GameBoard.getInstance("PlayerOne", "PlayerTwo");

        cell = GameBoard.getHexCell(1,0);

        minion = new Minion(board.getPlayerOne(), cell);
        board.buyMinionForPlayerOne(cell);
    }

    @Test
    void testMinionInitialization() {
        assertNotNull(minion);
        assertEquals(board.getPlayerOne(), minion.getOwner());
        assertEquals(cell, minion.getPosition());
        assertEquals(GameConfig.InitHp + 10, minion.getHP());
        assertEquals(10, minion.getDef());
    }

    @Test
    void testMinionMoveValid() throws IOException {
        HexCell targetCell = GameBoard.getHexCell(1,1);

        minion.setPosition(1, 1);

        assertEquals(1, minion.getX());
        assertEquals(1, minion.getY());
        assertEquals(targetCell, minion.getPosition());
    }

    @Test
    void testMinionMoveToOccupiedCell() throws IOException {
        HexCell occupiedCell = GameBoard.getHexCell(1,2);

        // วางมินเนียนตัวใหม่ในตำแหน่ง (1,2)
        Minion otherMinion = new Minion(board.getPlayerTwo(), occupiedCell);
        occupiedCell.addMinion(otherMinion);

        HexCell targetCell = GameBoard.getHexCell(1,1);

        minion.setPosition(1, 1);

        assertEquals(1, minion.getX());
        assertEquals(1, minion.getY());

        // พยายามย้ายมินเนียนไปที่ (1,2) ซึ่งถูกยึดครองอยู่
        minion.setPosition(1, 2);

        // ควรยังอยู่ที่ตำแหน่งเดิม (1,1)
        assertEquals(1, minion.getX());
        assertEquals(1, minion.getY());
    }

    @Test
    void testMinionMoveOutOfBounds() throws IOException {
        HexCell targetCell = GameBoard.getHexCell(1,1);

        minion.setPosition(1, 1);

        assertEquals(1, minion.getX());
        assertEquals(1, minion.getY());

        minion.setPosition(-1, 2); // ตำแหน่งนี้ไม่ควรอยู่บนบอร์ด

        assertEquals(1, minion.getX());
        assertEquals(1, minion.getY());
    }
}
