package backend.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProcess {

    public void readStrategy(String fileName , Player player , Minion minion) throws IOException {
        Path path = Paths.get(fileName);
        Charset charset = Charset.forName("UTF-8");

        if (!Files.exists(path)) {
            System.err.println("Input file does not exist: " + fileName);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path,charset)){
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    Tokenizer token = new ExprTokenizer(line);
                    Parser parser = new ExprParse(token, player, minion);
                } catch (IllegalArgumentException x) {
                    System.out.println("Invalid operator in expression: " + line + " -> " + x.getMessage());
                } catch (ArithmeticException x) {
                    System.out.println("Error evaluating expression: " + line + " -> " + x.getMessage());
                }
            }
        }catch (IOException x){
        System.out.println("Error reading/writing file: " + x.getMessage());
        }
    }

    public void readConfig(String filename) throws IOException {
        Path path = Paths.get(filename);
        Charset charset = Charset.forName("UTF-8");

        try(BufferedReader reader = Files.newBufferedReader(path, charset)) {

            String line;
            GameRule gameRule = new GameRule();
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
