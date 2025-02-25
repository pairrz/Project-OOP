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

public class FileProcess{
    private final Map<String, Integer> playerBindings = new HashMap<>();


    public void readStrategy(String fileName, Minion minion) throws IOException {
        Path path = Paths.get(fileName);
        Charset charset = StandardCharsets.UTF_8;

        if (!Files.exists(path)) {
            System.err.println("Input file does not exist: " + fileName);
            return;
        }

        // ✅ ใช้ StringBuilder เพื่อรวมทุกบรรทัดเป็นสตริงเดียว
        StringBuilder strategyBuilder = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                strategyBuilder.append(line).append(" ");  // ✅ เพิ่มช่องว่างเพื่อแยกโทเคน
            }
        } catch (IOException x) {
            System.out.println("Error reading file: " + x.getMessage());
            return;
        }

        // ✅ สร้าง Tokenizer จากสตริงทั้งหมดของกลยุทธ์
        String strategyCode = strategyBuilder.toString().trim();
        if (strategyCode.isEmpty()) {
            System.out.println("Strategy file is empty.");
            return;
        }

        try {
            Tokenizer token = new ExprTokenizer(strategyCode);  // ✅ Tokenizer อ่านทั้งไฟล์
            Parser parser = new ExprParse(token, minion);
            Expr expr = parser.parse();
            expr.eval(playerBindings);
        } catch (IllegalArgumentException x) {
            System.out.println("Invalid operator in expression: " + x.getMessage());
        } catch (Exception x) {
            System.out.println("Error evaluating expression: " + x.getMessage());
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
