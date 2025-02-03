import java.util.Map;

public class InfoExpr implements Expr {
    String string;

    public InfoExpr(String string) {
        this.string = string;
    }

    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        return 0;
    }
}
