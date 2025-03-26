package project.kombat_backend.model.minion;

import project.kombat_backend.config.GameConfig;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.model.player.Player;

public class Ghost5 extends Minion {

    public Ghost5(Player player, HexCell cell, GameConfig gameConfig) {
        super(player, cell, gameConfig);
        this.setBonusDef(50);
    }
}
