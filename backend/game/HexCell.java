package backend.game;

import backend.minions.*;
import backend.players.*;

public class HexCell {
    private int x;
    private int y;
    private String status;
    /*
        - hex cell ว่าง
        1 ช่องของ py1
        2 ช่องของ py2
        * ช่องของมินเนียน py1
        # ช่องของมินเนียน py2
     */
    private Player owner;  // เจ้าของเซลล์
    private Minion minion; // มินเนี่ยนในเซลล์

    public HexCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.minion = null;
        this.owner = null;
        this.status = "-";
    }

    // ตั้งค่าเจ้าของเซลล์
    public void setOwner(Player player) {
        this.owner = player;
    }

    public  String getStatus() {
        return status;
    }

    // ตั้งค่า backend.minions.Minion ในเซลล์
    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ลบ backend.minions.Minion ออกจากเซลล์
    public void removeMinion() {
        this.minion = null;
    }

    // เช็คว่าเซลล์ถูกยึดครองหรือไม่
    public boolean isOccupied() {
        return owner != null || minion != null;
    }

    // เช็คว่าเซลล์ว่างหรือไม่ (ไม่มี backend.minions.Minion)
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

//    // ฟังก์ชันนี้ตั้งค่าให้เซลล์ถูกยึดครองหรือไม่ (โดยตั้งค่าเจ้าของ)
//    public void setOccupied(boolean occupied) {
//        if (occupied) {
//
//        } else {
//            // ถ้าต้องการให้เซลล์ว่าง ให้ลบเจ้าของ
//            this.owner = null;
//        }
//    }
}
