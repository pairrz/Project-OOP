//package backend.minions;
//
//import backend.game.*;
//import backend.players.*;
//
//import java.io.IOException;
//
//public class GiantMinion implements Minion {
//    //String name;
//    int hp;
//    int def;
//    int x;
//    int y;
//    Player owner = null;
//
//    int bonusHP = 40;
//    int bonusDef = 10;
//
//    public GiantMinion() {
//        //this.name = name;
//        this.hp = GameConfig.InitHp + bonusHP;
//        this.def = bonusDef;
//    }
//
//    @Override
//    public void minionStrategy(String text) throws IOException {
//        FileProcess file = new FileProcess();
//        file.readStrategy(text,this);
//    }
//
//    @Override
//    public int getHP() {
//        return hp;
//    }
//
//    @Override
//    public int getDef() {
//        return def;
//    }
//
//    @Override
//    public int getX() {
//        return x;
//    }
//
//    @Override
//    public int getY() {
//        return y;
//    }
//
//    @Override
//    public Player getOwner() {
//        return owner;
//    }
//
//    @Override
//    public HexCell getPosition() {
//        return new HexCell(x, y);
//    }
//
//    @Override
//    public void setPosition(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    @Override
//    public void setHP(int hp) {
//        this.hp = hp;
//    }
//}
