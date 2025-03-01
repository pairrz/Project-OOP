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

    public void readStrategy(String fileName, Minion minion) throws IOException {
        Path path = Paths.get(fileName);
        Charset charset = StandardCharsets.UTF_8;

        if (!Files.exists(path)) {
            System.err.println("Input file does not exist: " + fileName);
            return;
        }

        StringBuilder fullContent = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    fullContent.append(line).append(" "); // รวมบรรทัดเข้าด้วยกัน
                }
            }
        }

        if (fullContent.isEmpty()) {
            System.err.println("Strategy file is empty or only contains whitespace.");
            return;
        }

        try {
            //System.out.println("Parsing strategy: " + fullContent);

            Tokenizer token = new ExprTokenizer(fullContent.toString());
            if (!token.hasNextToken()) {
                //System.out.println("No valid tokens in strategy.");
                return;
            }
            Parser parser = new ExprParse(token, minion);
            Expr expr = parser.parse();
            expr.eval(bindings);
        } catch (IllegalArgumentException x) {
            System.out.println("Invalid operator in strategy: " + x.getMessage());
        } catch (ArithmeticException x) {
            System.out.println("Error evaluating strategy: " + x.getMessage());
        } catch (Exception x) {
            System.out.println("Error processing strategy: " + x.getMessage());
        }
    }

//    public void readStrategy(String fileName, Minion minion) throws IOException {
//        Path path = Paths.get(fileName);
//        Charset charset = StandardCharsets.UTF_8;
//        System.out.println("reading strategy");
//
//        if (!Files.exists(path)) {
//            System.err.println("Input file does not exist: " + fileName);
//            return;
//        }
//
//        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                line = line.trim();
//                if (line.isEmpty()) continue; // ข้ามบรรทัดว่าง
//
//                try {
//                    System.out.println("Parsing line: " + line);
//
//                    Tokenizer token = new ExprTokenizer(line);
//                    if (!token.hasNextToken()) {
//                        System.out.println("Skipping empty tokenized line: " + line);
//                        continue;
//                    }
//
//                    Parser parser = new ExprParse(token, minion);
//                    Expr expr = parser.parse();
//                    expr.eval(bindings);
//                } catch (IllegalArgumentException x) {
//                    System.out.println("Invalid operator in expression: " + line + " -> " + x.getMessage());
//                } catch (ArithmeticException x) {
//                    System.out.println("Error evaluating expression: " + line + " -> " + x.getMessage());
//                }
//            }
//        } catch (Exception x) {
//            System.out.println("Error reading/writing file: " + x.getMessage());
//        }
//    }


    public void readConfig(String filename) throws IOException {
        Path path = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String var = parts[0].trim().toLowerCase().replaceAll("\\s", ""); // Normalize
                    String val = parts[1].trim();

                    try {
                        int value = Integer.parseInt(val);
                        GameConfig.assign(var, value);
                        //System.out.println("Config Loaded: " + var + " = " + value); // Debug Log
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format in config: " + line);
                    }
                } else {
                    System.out.println("Invalid config line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading config file: " + e.getMessage());
        }

        //GameConfig.printVar();
    }

}
