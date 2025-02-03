public class HumanMinion implements Minion {
    String name;
    int hp;
    int def;
    int x;
    int y;

    int bonusHP = 10;
    int bonusDef = 10;

    public HumanMinion(String name) {
        this.name = name;
        this.hp = GameRule.InitHp + bonusHP;
        this.def = bonusDef;
    }

    @Override
    public int getHp() {
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
