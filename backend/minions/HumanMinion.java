package backend.minions;

import backend.game.*;
import backend.players.*;

public class HumanMinion implements Minion {
    //String name;
    int hp;
    int def;
    int x;
    int y;
    int bonusHP = 10;
    int bonusDef = 10;

    public HumanMinion() {
        //this.name = name;
        this.hp = GameConfig.InitHp + bonusHP;
        this.def = bonusDef;
    }

    @Override
    public void minionStrategy(String string) {

    }

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public int getDef() {
        return def;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Player getOwner() {
        return null;
    }

    @Override
    public HexCell getPosition() {
        return new HexCell(x,y);
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setHex(HexCell hex) {
        this.x = hex.getX();
        this.y = hex.getY();
    }

    @Override
    public void setHP(int hp) {
        this.hp = hp;
    }
}
