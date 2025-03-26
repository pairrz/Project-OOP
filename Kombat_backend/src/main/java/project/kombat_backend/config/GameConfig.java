package project.kombat_backend.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Getter
@Configuration
public class GameConfig {

    private int spawnCost;
    private int hexPurchaseCost;
    private int initBudget;
    private int initHp;
    private int turnBudget;
    private int maxBudget;
    private int interestPct;
    private int maxTurns;
    private int maxSpawns;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        try {
            Resource resource = resourceLoader.getResource("classpath:config/Configuration.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue; // ข้ามค่าว่าง/คอมเมนต์

                String[] parts = line.split("=");
                if (parts.length != 2) continue;

                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "spawn_cost" -> spawnCost = Integer.parseInt(value);
                    case "hex_purchase_cost" -> hexPurchaseCost = Integer.parseInt(value);
                    case "init_budget" -> initBudget = Integer.parseInt(value);
                    case "init_hp" -> initHp = Integer.parseInt(value);
                    case "turn_budget" -> turnBudget = Integer.parseInt(value);
                    case "max_budget" -> maxBudget = Integer.parseInt(value);
                    case "interest_pct" -> interestPct = Integer.parseInt(value);
                    case "max_turns" -> maxTurns = Integer.parseInt(value);
                    case "max_spawns" -> maxSpawns = Integer.parseInt(value);
                }
            }
            reader.close();

            System.out.println("GameConfig Loaded Successfully");
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
    }
}
