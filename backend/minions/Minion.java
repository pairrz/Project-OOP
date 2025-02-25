package backend.minions;

import backend.game.*;
import backend.players.Player;
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
    protected HexCell cell;

    public Minion(Player owner, HexCell cell) {
        this.position = GameBoard.getHexCell(cell.getX(), cell.getY());
        this.x = cell.getX();
        this.y = cell.getY();
        position.addMinion(this);
        this.owner = owner;
        this.hp = 10;
        this.def = 0;
    }

    public void minionStrategy(String string) throws IOException{
        FileProcess file = new FileProcess();
        file.readStrategy(string, this);
    }

    public void checkPosition(int newX, int newY) throws IOException {
        GameBoard board = GameBoard.getInstance(GameBoard.namePlayerOne, GameBoard.namePlayerTwo);

        // ตรวจสอบว่าตำแหน่งใหม่อยู่ในขอบเขตบอร์ด
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
        setPosition(newPosition);

        // เพิ่มมินเนียนเข้าไปใน HexCell ใหม่
        this.position.addMinion(this);

        // อัปเดตสถานะกระดาน
        board.setStatus();
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

    public void moveTo(int newX, int newY){
        this.x = newX;
        this.y = newY;
        this.position = GameBoard.getHexCell(newX, newY);
    }

    public void setPosition(HexCell newPosition){
        this.position = newPosition;
        this.x = newPosition.getX();
        this.y = newPosition.getY();
    }
}