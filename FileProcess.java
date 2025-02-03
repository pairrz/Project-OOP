import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProcess {
    public void readfile(String filename) throws IOException {
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
