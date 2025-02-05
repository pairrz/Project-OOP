import java.util.ArrayList;

public class HexCell {
    private int x;
    private int y;
    private Player owner;  // เจ้าของเซลล์
    private Minion minion; // มินเนี่ยนในเซลล์

    public HexCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.minion = null;
        this.owner = null;
    }

    // ตั้งค่าเจ้าของเซลล์
    public void setOwner(Player player) {
        this.owner = player;
    }

    // ตั้งค่า Minion ในเซลล์
    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    // ลบ Minion ออกจากเซลล์
    public void removeMinion() {
        this.minion = null;
    }

    // เช็คว่าเซลล์ถูกยึดครองหรือไม่
    public boolean isOccupied() {
        return owner != null;  // เซลล์ถูกยึดครองถ้ามีเจ้าของ
    }

    // เช็คว่าเซลล์ว่างหรือไม่ (ไม่มี Minion)
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

    // ฟังก์ชันนี้ตั้งค่าให้เซลล์ถูกยึดครองหรือไม่ (โดยตั้งค่าเจ้าของ)
    public void setOccupied(boolean occupied) {
        if (occupied) {
            // ถ้าต้องการให้เซลล์ถูกยึดครอง ให้ตั้งค่าผู้เล่นเป็นเจ้าของ
            this.owner = new Player() {
                @Override
                public String getName() {
                    return "";
                }

                @Override
                public int getBudget() {
                    return 0;
                }

                @Override
                public int getNumber() {
                    return 0;
                }

                @Override
                public int getSumHP() {
                    return 0;
                }

                @Override
                public int getRate(int turn) {
                    return 0;
                }

                @Override
                public ArrayList<Minion> getMinions() {
                    return null;
                }

                @Override
                public ArrayList<HexCell> getHexCells() {
                    return null;
                }

                @Override
                public void setBudget(int budget) {

                }

                @Override
                public void buyMinion(Minion minion, boolean isMyHexCell) {

                }

                @Override
                public void buyHexCell(HexCell cell) {

                }

                @Override
                public void takeTurn(int turn) {

                }

                @Override
                public void removeMinion(int index) {

                }

                @Override
                public void setNumber(int i) {

                }

                @Override
                public void setSumHP(int i) {

                }
            };  // ใส่ผู้เล่นตามต้องการ
        } else {
            // ถ้าต้องการให้เซลล์ว่าง ให้ลบเจ้าของ
            this.owner = null;
        }
    }
}
