public class HumanMinion implements Minion {
    String name;
    int hp;
    int def;
    int x;
    int y;
    int bonusHP = 10;
    int bonusDef = 10;

    public HumanMinion(String name,HexCell hexCell) {
        this.name = name;
        this.hp = GameRule.InitHp + bonusHP;
        this.def = bonusDef;
        this.x = hexCell.getX();
        this.y = hexCell.getY();
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
        return new HexCell(x,y);
    }

    @Override
    public void setPosition(int x, int y) {

    }

    @Override
    public void setHP(int hp) {

    }
}
