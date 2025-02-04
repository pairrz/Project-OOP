public class SlaveMinion implements Minion {
    String name;
    int hp;
    int def;
    int x;
    int y;

    public SlaveMinion(String name) {
        this.name = name;
        this.hp = GameRule.InitHp;
        this.def = 0;
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
}
