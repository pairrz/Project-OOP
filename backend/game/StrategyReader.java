package backend.game;

import backend.ast.DoneExpr;
import backend.minions.Minion;
import backend.parser.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class StrategyReader {
    Map<String, Integer> bindings = new HashMap<>();

    public void readStrategyFromFile(String fileName, Minion minion) throws Exception {
        String content = readFile(fileName);
        processStrategy(content, minion);
    }

    public void readStrategyFromString(String strategyContent, Minion minion) throws Exception {
        processStrategy(strategyContent, minion);
    }

    private void processStrategy(String content, Minion minion) throws Exception {
            Tokenizer token = new ExprTokenizer(content);
            if (!token.hasNextToken()) {
                throw new IllegalArgumentException("Strategy content is invalid or empty.");
            }
            Parser parser = new ExprParse(token, minion);
            Expr expr = parser.parse();
            expr.eval(bindings);
    }

    private String readFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        StringBuilder fullContent = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    fullContent.append(line).append(" ");
                }
            }
        }

        if (fullContent.isEmpty()) {
            throw new IOException("Strategy file is empty or only contains whitespace.");
        }

        return fullContent.toString();
    }
}
