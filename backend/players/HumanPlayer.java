package backend.players;

import backend.game.*;
import backend.minions.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer implements Player {
    String name;
    int budget;
    ArrayList<HexCell> hexCells;
    ArrayList<Minion> minions;
    int numMinions;
    int baseR;

    int[][] directions = {
            {-1, 0},  // ซ้าย
            {1, 0},   // ขวา
            {0, -1},  // ขึ้น
            {0, 1},   // ลง
            {-1, 1},  // ซ้ายล่าง
            {1, -1}   // ขวาบน
    };

    public HumanPlayer(String name) {
        this.name = name;
        this.budget = GameConfig.InitBudget;
        this.hexCells = new ArrayList<>();
        this.minions = new ArrayList<>();
        this.numMinions = 0;
        this.baseR = GameConfig.InterestPct;
    }

    @Override
    public void takeTurn(int turn) throws IOException {
        int choice = 1;
        while (choice != 3){
            System.out.print("1.buy hex cell\n2.buy minion\n3.end turn\n");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();

            switch (choice){
                case 1:
                    buyHexCell();
                    break;
                case 2:
                    if(canBuyMinion(GameConfig.SpawnCost)){
                        System.out.print("enter hex cell position (x y): ");

                        Scanner scanner1 = new Scanner(System.in);
                        String input = scanner1.nextLine();

                        // แยกค่าพิกัด x และ y ด้วยช่องว่าง
                        String[] parts = input.split(" ");
                        int x = Integer.parseInt(parts[0]);  // พิกัด x
                        int y = Integer.parseInt(parts[1]);  // พิกัด y

                        HexCell cell = new HexCell(x, y);
                        if(isMyHexCell(cell)){
                            buyMinion(cell);
                        }
                    }else{
                        break;
                    }
                case 3:
                    break;
            }
        }

        for(int i = 0; i < numMinions; i++) {
            minions.get(i).minionStrategy("D:\\OOP project\\backend\\strategy\\Strategy.txt");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getBudget() {
        return budget;
    }

    @Override
    public void removeMinion(Minion minion) {
        minions.remove(minion);
        numMinions--;
    }

    @Override
    public int getNumber() {
        return numMinions;
    }

    @Override
    public int getSumHP() {
        int sum = 0;
        for(Minion minion : minions) {
            sum += minion.getHP();
        }
        return sum;
    }

    @Override
    public int getRate(int turn) {
        return (int) (baseR * Math.log10(Double.valueOf(budget)) * Math.log(Double.valueOf(turn)));
    }

    @Override
    public ArrayList<Minion> getMinions() {
        return minions;
    }

    @Override
    public ArrayList<HexCell> getHexCells() {
        return hexCells;
    }

    @Override
    public boolean isAdjacent(HexCell targetCell) {
        int targetX = targetCell.getX();
        int targetY = targetCell.getY();

        // วนลูปเช็คทุกเซลล์ในลิสต์
        for (HexCell hex : hexCells) {
            int x = hex.getX();
            int y = hex.getY();

            // ตรวจสอบทิศทางที่ติดกับเซลล์ในลิสต์
            for (int[] direction : directions) {
                int nearX = x + direction[0];
                int nearY = y + direction[1];

                // ตรวจสอบว่าเซลล์ที่รับมาติดกับเซลล์ในลิสต์หรือไม่
                if (nearX == targetX && nearY == targetY) {
                    return true; // ถ้าพบว่าติดกัน
                }
            }
        }
        return false; // ถ้าไม่พบว่าเซลล์ที่รับมาติดกับเซลล์ใดในลิสต์
    }

    @Override
    public boolean isMyHexCell(HexCell cell) {
        return hexCells.contains(cell);
    }

    @Override
    public void setBudget(int turn) {
        if(turn > 1){
            int result = budget * (int) getRate(turn) / 100;
            budget = result;
        }
    }

    @Override
    public void buyMinion(HexCell cell) {
        Minion minion = new HumanMinion();
        minions.add(minion);
        numMinions++;
        cell.setMinion(minion);
    }

    private boolean canBuyMinion(int cost) {
        return budget >= cost;
    }

    @Override
    public void buyHexCell() {
        if(budget >= GameConfig.HexPurchase) {
            System.out.print("enter hex cell position (x y): ");

            Scanner scanner1 = new Scanner(System.in);
            String input = scanner1.nextLine();

            // แยกค่าพิกัด x และ y ด้วยช่องว่าง
            String[] parts = input.split(" ");
            int x = Integer.parseInt(parts[0]);  // พิกัด x
            int y = Integer.parseInt(parts[1]);  // พิกัด y

            HexCell cell = new HexCell(x, y);
            if(isAdjacent(cell)) {
                budget -= GameConfig.HexPurchase;
                hexCells.add(cell);
            }
        }else{
            System.out.println("You do not have enough bidget to buy a hex cell");
        }
    }

    @Override
    public void setNumber(int i) {

    }

    @Override
    public void setSumHP(int i) {

    }

    public void allHex(){
        for (HexCell hexCell : hexCells) {
            System.out.println(hexCell.getX() + " " + hexCell.getY());
        }
    }
}
