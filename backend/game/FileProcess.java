package backend.game;

import backend.minions.*;
import backend.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileProcess {
    Map<String,Integer> bindings = new HashMap<>();
    public void readStrategy(String fileName,Minion minion) throws IOException {
        Path path = Paths.get(fileName);
        Charset charset = StandardCharsets.UTF_8;
        System.out.println("reading strategy");
        if (!Files.exists(path)) {
            System.err.println("Input file does not exist: " + fileName);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path,charset)){
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    Tokenizer token = new ExprTokenizer(line);
                    Parser parser = new ExprParse(token,minion);
                    Expr expr = parser.parse();
                    expr.eval(bindings);
                } catch (IllegalArgumentException x) {
                    System.out.println("Invalid operator in expression: " + line + " -> " + x.getMessage());
                } catch (ArithmeticException x) {
                    System.out.println("Error evaluating expression: " + line + " -> " + x.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (IOException x){
        System.out.println("Error reading/writing file: " + x.getMessage());
        }
    }

    public void readConfig(String filename) throws IOException {
        Path path = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("=");

                if (parts.length == 2) {
                    String var = parts[0].trim();
                    try {
                        int value = Integer.parseInt(parts[1].trim());
                        GameConfig.assign(var, value);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format in config: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading config file: " + e.getMessage());
        }
    }
}
