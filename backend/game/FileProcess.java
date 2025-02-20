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
import java.util.Map;

public class FileProcess{
    private Map<String, Integer> playerBindings;

    public void readStrategy(String fileName, Minion minion) throws IOException{
        Path path = Paths.get(fileName);
        Charset charset = StandardCharsets.UTF_8;

        if (!Files.exists(path)) {
            System.err.println("Input file does not exist: " + fileName);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path,charset)){
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    Tokenizer token = new ExprTokenizer(line);
                    Parser parser = new ExprParse(token, minion);
                    Expr expr = parser.parse();
                    expr.eval(playerBindings);
                } catch (IllegalArgumentException x) {
                    System.out.println("Invalid operator in expression: " + line + " -> " + x.getMessage());
                } catch (Exception x) {
                    System.out.println("Error evaluating expression: " + line + " -> " + x.getMessage());
                }
            }
        }catch (IOException x){
        System.out.println("Error reading/writing file: " + x.getMessage());
        }
    }

    public void readConfig(String filename) throws IOException {
        Path path = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;

        try(BufferedReader reader = Files.newBufferedReader(path, charset)) {

            String line;
            GameConfig gameRule = new GameConfig();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("=");
                if (parts.length == 2) {
                    String var = parts[0].trim();
                    int value = Integer.parseInt(parts[1].trim());
                    gameRule.assign(var,value);
                }
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
