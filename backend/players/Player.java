package backend.players;

import backend.game.*;
import backend.minions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Player {
    protected static final Scanner scanner = new Scanner(System.in);
    protected final String name;
    protected final Map<String, HexCell> hexCells;
    protected final ArrayList<Minion> minions;
    protected int budget;
    protected final int baseR;

    public Player(String name, Map<String, HexCell> hexCells) {
        this.name = name;
        this.hexCells = hexCells;
        this.budget = GameConfig.InitBudget;
        this.minions = new ArrayList<>();
        this.baseR = GameConfig.InterestPct;
    }

    public void takeTurn(int turn) throws IOException {
        printStatus();

        boolean boughtHex = false;
        boolean boughtMinion = false;

        int choice = 0;

        while (choice != 3) {
            System.out.print("\n1.buy hex cell\n2.buy minion\n3.end turn\n");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (!boughtHex) {
                        HexCell cell = askHexcell();
                        buyHexCell(cell);
                        boughtHex = true;
                    } else {
                        System.out.println("You have already bought a hex cell this turn.");
                    }
                    break;
                case 2:
                    if (boughtMinion) {
                        System.out.println("You have already bought a minion this turn.");
                        break;
                    }
                    if (!canBuyMinion()) {
                        System.out.println("Not enough money!!!");
                        break;
                    }

                    HexCell cell = askHexcell();
                    if (isMyHex(cell)) {
                        buyMinion(cell);
                        boughtMinion = true;
                    } else {
                        System.out.println("Invalid cell! You can only buy minions on your own hex.");
                    }
                    break;
                case 3:
                    break;
            }
        }

        for (Minion minion : minions) {
            minion.minionStrategy("D:\\OOP project\\backend\\strategy\\Strategy2.txt");
        }

        printStatus();

        resetBudget(turn);
    }

    public void buyMinion(HexCell cell) {
        if (budget >= GameConfig.SpawnCost) {
            HexCell hexCell = GameBoard.getHexCell(cell.getX(), cell.getY());

            if (isMyHex(hexCell) && !hexCell.hasMinion()) {
                Minion minion = new Minion(this, hexCell);
                hexCell.addMinion(minion);

                System.out.println(budget + " -" + GameConfig.SpawnCost);
                budget -= GameConfig.SpawnCost;
                System.out.println(budget);
                minions.add(minion); // เพิ่มมินเนียนเข้าไปใน List ของผู้เล่น

                System.out.println("มินเนียนถูกวางใน HexCell (" + cell.getX() + "," + cell.getY() + ")");
            } else {
                System.out.println("ไม่สามารถวางมินเนียนที่นี่ได้!" + cell.getX() + "," + cell.getY());
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
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public boolean canBuyMinion() {
        return (budget >= GameConfig.SpawnCost);
    }

    private HexCell askHexcell() {
        System.out.print("Enter hex cell position (x y): ");
        String input = scanner.nextLine();
        String[] parts = input.split(" ");

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            return GameBoard.getHexCell(x, y);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers.");
            return askHexcell();
        }
    }

    public boolean isMyHex(HexCell cell) {
        return this.hexCells.containsKey(cell.getX() + "," + cell.getY());
    }

    public boolean isAdjacent(HexCell cell) {
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

    public void printStatus() {
        System.out.println(name + "'s status :");
        System.out.println(budget + " coins");
        System.out.println(minions.size() + " minions");
    }

    public String getName() {
        return name;
    }

    public int getBudget() {
        return budget;
    }

    public int getNumMinions() {
        return minions.size();
    }

    public ArrayList<Minion> getMinions() {
        return minions;
    }

    public void resetBudget(int turn) {
        if (turn > 1) {
            budget += GameConfig.TurnBudget;
            double rate = getRate(turn) / 100.0; // คำนวณเป็นอัตราส่วน
            budget = (int) Math.floor(budget + (budget * rate)); // ปัดเศษลงเพื่อความแม่นยำ
            budget =  Math.min(budget, GameConfig.MaxBudget);
        }
    }

    public double getRate(int turn) {
        if (budget <= 0 || turn <= 1) return 0;
        return baseR * Math.log10(budget) * Math.log(turn);
    }

    public int getSumHP() {
        int sum = 0;
        for (Minion minion : minions) {
            sum += minion.getHP();
        }
        return sum;
    }
}