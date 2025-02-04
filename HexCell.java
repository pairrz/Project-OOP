public class HexCell {
    private int x;
    private int y;
    private Player owner;
    private Minion minion;

    public HexCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.minion = null;
        this.owner = null;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    public void removeMinion() {
        this.minion = null;
    }

    public boolean isOccupied() {
        return owner != null;
    }

    public boolean isEmpty() {
        return minion == null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getOwner() {
        return owner;
    }

    public Minion getMinion() {
        return minion;
    }
}
