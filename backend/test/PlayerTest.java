package backend.test;

import backend.game.*;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    void buyMinion(){
        GameBoard g = new GameBoard("first","second");
        HexCell cell1 = new HexCell(0,0);
        HexCell cell2 = new HexCell(0,1);
        HexCell cell3 = new HexCell(7,7);
        HexCell cell4 = new HexCell(6,7);
        g.showBoard();
        g.onebymn(cell1);
        g.onebymn(cell2);
        g.onebymn(cell3);
        g.secbymin(cell4);
        g.setStatus();
        g.showBoard();
    }
}
