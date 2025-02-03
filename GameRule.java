public class GameRule {
    public static int SpawnCost;
    public static int HexPurchase;
    public static int InitBudget;
    public static int InitHp;
    public static int TurnBudget;
    public static int MaxBudget;
    public static int InterestPct;
    public static int MaxTurns;
    public static int MaxSpawns;

    public void assign(String variable,int x){
        switch (variable) {
            case "spawn_cost" -> SpawnCost = x;
            case "hex_purchase" -> HexPurchase = x;
            case "init_budget" -> InitBudget = x;
            case "init_hp" -> InitHp = x;
            case "turn_budget" -> TurnBudget = x;
            case "max_budget" -> MaxBudget = x;
            case "interest_pct" -> InterestPct = x;
            case "max_turns" -> MaxTurns = x;
            case "max_spawns" -> MaxSpawns = x;
        }
    }

}
