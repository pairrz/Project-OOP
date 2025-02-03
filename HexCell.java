public class HexCell {
    private int x;
    private int y;
    private Minion minion;

    public HexCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.minion = null; // ไม่มีมินเนียนตอนเริ่มต้น
    }

    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    public void removeMinion() {
        this.minion = null;
    }

    public boolean isOccupied() {
        return minion != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Minion getMinion() {
        return minion;
    }
}
