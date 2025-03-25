package backend.players;

import backend.game.*;
import backend.minions.*;
import java.io.IOException;
import java.util.*;

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
                        canBuyCell();
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
            minion.minionStrategy(GameBoard.Strategy);
        }

        printStatus();

        resetBudget(turn);
    }

    public void buyMinion(HexCell cell) {
        if (budget >= GameConfig.SpawnCost) {
            HexCell hexCell = GameBoard.getHexCell(cell.getX(), cell.getY());

            if (isMyHex(hexCell) && !hexCell.hasMinion()) {
                Minion minion = chooseType(hexCell);
                hexCell.addMinion(minion);

                budget -= GameConfig.SpawnCost;
                minions.add(minion);

                System.out.println("มินเนียนถูกวางใน HexCell (" + cell.getX() + "," + cell.getY() + ")");
            } else {
                System.out.println("ไม่สามารถวางมินเนียนที่นี่ได้!" + cell.getX() + "," + cell.getY());
            }
        } else {
            System.out.println("งบประมาณไม่พอ!");
        }
    }

    public Minion chooseType(HexCell cell) {
        if (GameManage.selectedMinions == null) {
            System.out.println("Error: No minions selected!");
            return null;
        }

        for(String str : GameManage.selectedMinions){
            switch (str){
                case "1":
                    System.out.println("1.LordMinion");
                    break;
                case "2":
                    System.out.println("2.GiantMinion");
                    break;
                case "3":
                    System.out.println("3.WarriorMinion");
                    break;
                case "4":
                    System.out.println("4.HumanMinion");
                    break;
                case "5":
                    System.out.println("5.SlaveMinion");
                    break;
            }
        }
        System.out.println("Choose a minion type to spawn: ");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        return switch (str) {
            case "1" -> new LordMinion(this, cell);
            case "2" -> new GiantMinion(this, cell);
            case "3" -> new WarriorMinion(this, cell);
            case "4" -> new HumanMinion(this, cell);
            case "5" -> new SlaveMinion(this, cell);
            default -> null;
        };
    }

    public void buyHexCell(HexCell cell) {
        if (budget >= GameConfig.HexPurchase) {
            HexCell hexCell = GameBoard.getHexCell(cell.getX(), cell.getY());

            if (isAdjacent(hexCell) && !GameBoard.isOccupied(cell.getX(), cell.getY())) {
                budget -= GameConfig.HexPurchase;
                hexCell.setOwner(this);

                hexCells.put(cell.getX() + "," + cell.getY(), hexCell);
                System.out.println("HexCell (" + cell.getX() + "," + cell.getY() + ") ถูกซื้อสำเร็จ!");
                GameBoard.setOccupied(cell.getX(), cell.getY());
            } else {
                System.out.println("HexCell นี้ซื้อไม่ได้!");
            }
        } else {
            System.out.println("งบประมาณไม่พอ!");
        }
    }

    public void canBuyCell() {
        Set<HexCell> availableCells = new HashSet<>();

        for (HexCell cell : GameBoard.hexCellMap.values()) {
            if (!hexCells.containsKey(cell.getKey()) && isAdjacent(cell) && GameBoard.isValidPosition(cell.getX(), cell.getY())) {
                availableCells.add(cell);
            }
        }

        if (availableCells.isEmpty()) {
            System.out.println("ไม่มี HexCell ที่สามารถซื้อได้");
        } else {
            System.out.println("HexCell ที่สามารถซื้อได้:");
            for (HexCell cell : availableCells) {
                System.out.println(cell.getKey());
            }
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

        int[][] oddRowDirections = {{-1, 0}, {-1, 1}, {0, 1}, {1, 0}, {0, -1}, {-1, -1}};
        int[][] evenRowDirections = {{-1, 0}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};
        int[][] directions = (y % 2 == 0) ? evenRowDirections : oddRowDirections;

        for (int[] dir : directions) {
            int neighborX = x + dir[0];
            int neighborY = y + dir[1];
            String neighborKey = neighborX + "," + neighborY;

            if (hexCells.containsKey(neighborKey)) {
                return true;
            }
        }

        return false;
    }

    public Map<String, HexCell> getHexCells() {
        return hexCells;
    }

    public void printStatus() {
        System.out.println();
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
            double rate = getRate(turn) / 100.0;
            budget = (int) Math.floor(budget + (budget * rate));
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