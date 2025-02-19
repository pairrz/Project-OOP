package backend.minions;

import backend.game.*;
import backend.players.*;

import java.io.IOException;

public interface Minion {
    void minionStrategy(String string) throws IOException;
    int getHP();
    int getDef();
    int getX();
    int getY();
    Player getOwner();
    HexCell getPosition();
    void setPosition(int x, int y);
    void setHex(HexCell hex);
    void setHP(int hp);
}