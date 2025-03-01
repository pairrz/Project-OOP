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
        board.buyMinionForPlayerOne(testHex);

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

        board.showBoard(); //คนละ 5

        board.buyHexForPlayerOne(cell5); //ture
        board.buyHexForPlayerOne(cell6); //true
        board.buyHexForPlayerOne(cell7); //false
        board.buyHexForPlayerTwo(cell8); //true

        board.showBoard(); // one +2 , two +1

        board.buyMinionForPlayerOne(cell1); //true
        board.buyMinionForPlayerOne(cell2); //true
        board.buyMinionForPlayerOne(cell3); //false
        board.buyMinionForPlayerTwo(cell4); //true

        board.showBoard(); // one +2 , two +1
    }
}
