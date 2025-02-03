import java.util.Map;

public class NearbyExpr implements Expr {
    String direction;

    public NearbyExpr(String direction) {
        this.direction = direction;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        return 0;
    }
}
