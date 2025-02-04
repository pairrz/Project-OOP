public interface Minion {
    int getHP();
    int getDef();
    int getX();
    int getY();
    int getIndex();
    Player getOwner();
    void setIndex(int index);
    void setPosition(int x, int y);
    void setHP(int hp);
}