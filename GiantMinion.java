public class GiantMinion implements Minion {
    String name;
    int hp;
    int def;
    int x;
    int y;

    int bonusHP = 40;
    int bonusDef = 10;

    public GiantMinion(String name) {
        this.name = name;
        this.hp = GameRule.InitHp + bonusHP;
        this.def = bonusDef;
    }

    @Override
    public void minionStrategy(String strategy) {

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
        return new HexCell(x, y);
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setHP(int hp) {

    }
}
