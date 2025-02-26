package backend.test;

import backend.game.*;
import backend.minions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PlayerTest {

    @Test
    void buyHex() throws IOException {

            GameBoard board = GameBoard.getInstance("Alice", "Bob");
            board.showBoard();

            HexCell testHex = GameBoard.getHexCell(1, 2);
            board.getPlayerOne().buyHexCell(testHex);

            board.showBoard();
            board.showHexOne();

    }

    @Test
    void buyMinion() throws IOException {
        GameBoard board = GameBoard.getInstance("Alice", "Bob"); // แก้ไขตรงนี้
        HexCell testHex = GameBoard.getHexCell(1, 1);
        board.buyHexForPlayerOne(testHex);
        Minion minion = new Minion(board.getPlayerOne(), testHex);
        board.buyMinionForPlayerOne(testHex,minion);

        board.showBoard();
    }

    @Test
    void buyTwo() throws IOException {
        GameBoard board = GameBoard.getInstance("first", "second"); // แก้ไขตรงนี้
        HexCell cell1 = new HexCell(0, 0);
        HexCell cell2 = new HexCell(0, 1);
        HexCell cell3 = new HexCell(7, 7);
        HexCell cell4 = new HexCell(6, 7);

        HexCell cell5 = new HexCell(1, 2);
        HexCell cell6 = new HexCell(1, 3);
        HexCell cell7 = new HexCell(6, 5);
        HexCell cell8 = new HexCell(6, 4);

        board.showBoard();

        board.buyHexForPlayerOne(cell5);
        board.buyHexForPlayerOne(cell6);
        board.buyHexForPlayerOne(cell7);
        board.buyHexForPlayerTwo(cell8);

        board.showBoard();

        Minion minion1 = new Minion(board.getPlayerOne(), cell1);
        Minion minion2 = new Minion(board.getPlayerOne(), cell2);
        Minion minion3 = new Minion(board.getPlayerOne(), cell3);
        Minion minion4 = new Minion(board.getPlayerTwo(), cell4);

        board.buyMinionForPlayerOne(cell1,minion1);
        board.buyMinionForPlayerOne(cell2,minion2);
        board.buyMinionForPlayerOne(cell3,minion3);
        board.buyMinionForPlayerTwo(cell4,minion4);

        board.showBoard();
    }

}
