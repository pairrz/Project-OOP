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
