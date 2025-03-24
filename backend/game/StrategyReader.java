package backend.game;

import backend.minions.Minion;
import backend.parser.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class StrategyReader {
    Map<String,Integer> bindings = new HashMap<>();

    public void readStrategyFromFile(String fileName, Minion minion) throws IOException {
        if (!Files.exists(Paths.get(fileName))) {
            System.err.println("Input file does not exist: " + fileName);
            return;
        }
        String content = readFile(fileName);
        processStrategy(content, minion);
    }

    public void readStrategyFromString(String strategyContent, Minion minion) {
        if (strategyContent == null || strategyContent.trim().isEmpty()) {
            System.err.println("Strategy content is empty or null.");
            return;
        }
        processStrategy(strategyContent, minion);
    }

    private void processStrategy(String content, Minion minion) {
        try {
            Tokenizer token = new ExprTokenizer(content);
            if (!token.hasNextToken()) {
                return;
            }
            Parser parser = new ExprParse(token, minion);
            Expr expr = parser.parse();
            expr.eval(bindings);
        } catch (IllegalArgumentException x) {
            //System.out.println("Invalid operator in strategy: " + x.getMessage());
        } catch (ArithmeticException x) {
            //System.out.println("Error evaluating strategy: " + x.getMessage());
        } catch (Exception x) {
            //System.out.println("Error processing strategy: " + x.getMessage());
        }
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
            System.err.println("Strategy file is empty or only contains whitespace.");
        }

        return fullContent.toString();
    }
}
