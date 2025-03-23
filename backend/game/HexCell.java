package backend.game;

import backend.minions.*;
import backend.players.*;
import java.util.Objects;

public class HexCell {
    private final int x;
    private final int y;
    private String status;
    /*
        - hex cell ว่าง
        1 ช่องของ py1
        2 ช่องของ py2
        * ช่องของมินเนียน py1
        # ช่องของมินเนียน py2
     */
    private Player owner;  // เจ้าของเซลล์ฟ
    private Minion minion; // มินเนี่ยนในเซลล์

    public HexCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.minion = null;
        this.owner = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HexCell hexCell = (HexCell) obj;
        return x == hexCell.x && y == hexCell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public String getStatus() {
        return status;
    }

    public void addMinion(Minion minion) {
        this.minion = minion;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void removeMinion() {
        this.minion = null;
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

    public boolean hasMinion() {
        return this.minion != null;
    }

    public String getKey() {
        return x + "," + y;
    }
}
