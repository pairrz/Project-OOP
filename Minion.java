public interface Minion {
    void minionStrategy(String string);
    int getHP();
    int getDef();
    int getX();
    int getY();
    int getIndex();
    Player getOwner();
    void setPosition(int x, int y);
    void setHP(int hp);
}