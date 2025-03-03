package backend.minions;

import backend.game.*;
import backend.players.*;
import java.io.IOException;

public class Minion {
    protected int hp;
    protected int def;
    protected int x;
    protected int y;
    protected HexCell position;
    protected int bonusHP = 10;
    protected int bonusDef = 10;
    protected Player owner;

    public Minion(Player owner, HexCell cell) {
        this.position = cell;
        this.x = cell.getX();
        this.y = cell.getY();
        this.owner = owner;
        System.out.println(owner.getName());
        this.hp = GameConfig.InitHp + bonusHP;
        this.def = bonusDef;
        addMinion(cell);
    }

    public void minionStrategy(String string) throws IOException{
        FileProcess file = new FileProcess();
        file.readStrategy(string, this);
    }

    public void setPosition(int newX, int newY) throws IOException {
        // ตรวจสอบตำแหน่งใหม่อยู่ในบอร์ด
        if (!GameBoard.isValidPosition(newX, newY)) {
            System.out.println("ตำแหน่งใหม่อยู่นอกบอร์ด!");
            return;
        }

        // ดึง HexCell ใหม่
        HexCell newPosition = GameBoard.getHexCell(newX, newY);

        // ตรวจสอบว่าตำแหน่งใหม่ถูกยึดครองอยู่หรือไม่
        if (newPosition.hasMinion()) {
            System.out.println("ตำแหน่งนี้มีมินเนียนอยู่แล้ว ไม่สามารถย้ายได้!");
            return;
        }

        // ลบมินเนียนออกจากตำแหน่งเก่า
        if (this.position != null) {
            this.position.removeMinion();
        }

        // อัปเดตพิกัดของมินเนียน
        this.position = newPosition;
        setXY(newPosition);

        // เพิ่มมินเนียนเข้าไปใน HexCell ใหม่
        this.position.addMinion(this);
    }

    public HexCell getPosition() {
        return position;
    }

    public int getHP(){
        return hp;
    }

    public int getDef(){
        return def;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Player getOwner(){
        return owner;
    }

    public void setHP(int hp){
        this.hp = hp;
    }

    public void setXY(HexCell newPosition){
        this.x = newPosition.getX();
        this.y = newPosition.getY();
    }

    public void addMinion(HexCell hexCell){
        HexCell cell = GameBoard.getHexCell(hexCell.getX(), hexCell.getY());
        cell.addMinion(this);
    }
}