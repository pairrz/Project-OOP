package backend;

import backend.game.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameManage gameManage = new GameManage();
        gameManage.gamePlay();
    }
}