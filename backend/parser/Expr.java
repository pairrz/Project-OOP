package backend.parser;

import java.util.Map;

public interface Expr{
    int eval(Map<String, Integer> bindings) throws Exception;
}
