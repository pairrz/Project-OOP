package backend.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProcess {
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
    }
}
