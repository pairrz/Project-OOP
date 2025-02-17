package backend.players;

import backend.minions.Minion;

import java.util.ArrayList;

public class AiPlayer implements Player {
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
        // การทำงานของฟังก์ชัน removeMinion ที่จะลบ backend.minions.Minion ตาม id ที่รับมา
        // คุณสามารถกำหนด logic ของการลบ backend.minions.Minion ตามที่ต้องการ
        System.out.println("Removing backend.minions.Minion with ID: " + index);
    }

    @Override
    public void setNumber(int i) {

    }

    @Override
    public void setSumHP(int i) {

    }
}
