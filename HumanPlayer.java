import java.util.ArrayList;
import java.util.Queue;

public class HumanPlayer implements Player {
    private boolean isHuman = true;
    private String name;
    private int budget;
    Queue<Minion> minionsQ;
    ArrayList<HexCell> map;

    public HumanPlayer(String name) {
        this.name = name;
        this.budget = GameRule.InitBudget;
    }

    @Override
    public void takeTurn(){

    }
}
