package backend.players;

import backend.game.*;
import backend.minions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Player {
    protected final String name;
    protected final Map<String, HexCell> hexCells;
    protected final ArrayList<Minion> minions;
    protected int budget;
    protected int numMinions;
    protected final int baseR = GameConfig.InterestPct;

    public Player(String name, Map<String, HexCell> hexCells) throws IOException {
        this.name = name;
        this.hexCells = hexCells;
        this.budget = GameConfig.InitBudget;
        this.minions = new ArrayList<>();
    }

    public void takeTurn(int turn) throws IOException {
        System.out.println("Turn: " + turn);
        int choice = 1;
        while (choice != 3) {
            System.out.print("1.buy hex cell\n2.buy minion\n3.end turn\n");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter hex cell position (x y): ");

                    Scanner scanner1 = new Scanner(System.in);
                    String input1 = scanner1.nextLine();

                    // แยกค่าพิกัด x และ y ด้วยช่องว่าง
                    String[] parts1 = input1.split(" ");
                    int x = Integer.parseInt(parts1[0]) - 1;  // พิกัด x
                    int y = Integer.parseInt(parts1[1]) - 1;  // พิกัด y

                    HexCell cell = new HexCell(x, y);
                    buyHexCell(cell);

                    break;
                case 2:
                    System.out.print("Enter hex cell position (x y): ");

                    Scanner scanner2 = new Scanner(System.in);
                    String input2 = scanner2.nextLine();

                    // แยกค่าพิกัด x และ y ด้วยช่องว่าง
                    String[] parts2 = input2.split(" ");
                    int a = Integer.parseInt(parts2[0]) - 1;  // พิกัด x
                    int b = Integer.parseInt(parts2[1]) - 1;  // พิกัด y

                    HexCell cell2 = new HexCell(a, b);
                    buyMinion(cell2);

                default:
                    break;
            }

            for (Minion minion : minions) {
                System.out.print("in strategy minion ");
                minion.minionStrategy("D:\\OOP project\\backend\\strategy\\Strategy.txt");
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getBudget() {
        return budget;
    }

    public int getNumber() {
        return numMinions;
    }

    public int getSumHP() {
        int sum = 0;
        for (Minion minion : minions) {
            sum += minion.getHP();
        }
        return sum;
    }

    public int getRate(int turn) {
        return (int) (baseR * Math.log10((double) budget) * Math.log((double) turn));
    }

    public ArrayList<Minion> getMinions() {
        return minions;
    }

    private boolean isAdjacent(HexCell cell) {
        int x = cell.getX();
        int y = cell.getY();
        int[][] directions = {{-1, 0}, {-1, 1}, {0, 1}, {1, 0}, {0, -1}, {-1, -1}};

        for (HexCell hex : hexCells.values()) {
            for (int[] dir : directions) {
                if (hex.getX() + dir[0] == x && hex.getY() + dir[1] == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public void buyMinion(HexCell cell) {
        if (budget >= GameConfig.SpawnCost) {
            HexCell hexCell = GameBoard.getHexCell(cell.getX(), cell.getY());

            if (isMyHex(hexCell, hexCells) && !hexCell.hasMinion()) {
                Minion  minion = new Minion(this, hexCell);
                hexCell.addMinion(minion);

                budget -= GameConfig.SpawnCost;
                minions.add(minion); // เพิ่มมินเนียนเข้าไปใน List ของผู้เล่น

                System.out.println("มินเนียนถูกวางใน HexCell (" + (cell.getX()) + "," + (cell.getY()) + ")");
            } else {
                System.out.println("ไม่สามารถวางมินเนียนที่นี่ได้!");
            }
        } else {
            System.out.println("งบประมาณไม่พอ!");
        }
    }



    public void buyHexCell(HexCell cell) {
        if (budget >= GameConfig.HexPurchase) {
            HexCell hexCell = GameBoard.getHexCell(cell.getX(), cell.getY());

            if (isAdjacent(hexCell)) {
                budget -= GameConfig.HexPurchase;
                hexCell.setOwner(this);

                hexCells.put(cell.getX() + "," + cell.getY(), hexCell);
                //board.setStatus();
                System.out.println("HexCell (" + cell.getX() + "," + cell.getY() + ") ถูกซื้อสำเร็จ!");
            } else {
                System.out.println("HexCell นี้ซื้อไม่ได้!");
            }
        } else {
            System.out.println("งบประมาณไม่พอ!");
        }
    }

    public void removeMinion(Minion minion) {
        minions.remove(minion);
        numMinions--;
    }

    public void calBudget(int turn) {
        if (turn > 1) {
            budget = budget * (int) getRate(turn) / 100;
        }
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setSumHP(int i) {

    }

    public boolean canBuyMinion(HexCell cell, Map<String, HexCell> hexCells) {
        return (budget >= GameConfig.SpawnCost) && isMyHex(cell, hexCells); // ตรวจสอบว่าเป็น hex ของผู้เล่น
    }

    public boolean isMyHex(HexCell cell, Map<String, HexCell> hexCells) {
        return this.hexCells.containsKey(cell.getX() + "," + cell.getY());
    }


    public void getHex(HexCell cell){
        HexCell hexCell = GameBoard.getHexCell(cell.getX(), cell.getY());
        hexCells.put(cell.getX() + "," + cell.getY(), hexCell);
    }
}



