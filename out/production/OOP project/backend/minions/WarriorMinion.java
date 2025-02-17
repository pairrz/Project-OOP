package backend.minions;

public class WarriorMinion implements Minion {
    String name;
    int hp;
    int def;
    int x;
    int y;

    int bonusHP = 10;
    int bonusDef = 30;

    public WarriorMinion(String name) {
        this.name = name;
        this.hp = GameRule.InitHp + bonusHP;
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
    public int getIndex() {
        return 0;
    }

    @Override
    public Player getOwner() {
        return null;
    }

    @Override
    public HexCell getPosition() {
        return null;
    }

    @Override
    public void setPosition(int x, int y) {

    }

    @Override
    public void setHP(int hp) {

    }
}
