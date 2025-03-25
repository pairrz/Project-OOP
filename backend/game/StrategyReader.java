package backend.game;

import backend.minions.Minion;
import backend.parser.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class StrategyReader {
    Map<String, Integer> bindings = new HashMap<>();

    public void readStrategyFromFile(String fileName, Minion minion) throws IOException, StrategyEvaluationException, StrategyProcessingException, InvalidStrategyException {
        if (!Files.exists(Paths.get(fileName))) {
            throw new FileNotFoundException("Input file does not exist: " + fileName);
        }
        String content = readFile(fileName);
        processStrategy(content, minion);
    }

    public void readStrategyFromString(String strategyContent, Minion minion) throws StrategyEvaluationException, StrategyProcessingException, InvalidStrategyException {
        if (strategyContent == null || strategyContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Strategy content is empty or null.");
        }
        processStrategy(strategyContent, minion);
    }

    private void processStrategy(String content, Minion minion) throws InvalidStrategyException, StrategyEvaluationException, StrategyProcessingException {
        try {
            Tokenizer token = new ExprTokenizer(content);
            if (!token.hasNextToken()) {
                throw new IllegalArgumentException("Strategy content is invalid or empty.");
            }
            Parser parser = new ExprParse(token, minion);
            Expr expr = parser.parse();
            expr.eval(bindings);
        } catch (IllegalArgumentException e) {
            //throw new InvalidStrategyException("Invalid operator or syntax in strategy: " + e.getMessage(), e);
        } catch (ArithmeticException e) {
            //throw new StrategyEvaluationException("Error evaluating strategy: " + e.getMessage(), e);
        } catch (Exception e) {
            //throw new StrategyProcessingException("Error processing strategy: " + e.getMessage(), e);
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
            throw new IOException("Strategy file is empty or only contains whitespace.");
        }

        return fullContent.toString();
    }
}
