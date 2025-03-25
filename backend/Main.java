package backend;

import backend.game.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, StrategyEvaluationException, StrategyProcessingException, InvalidStrategyException {
        GameManage gameManage = new GameManage();
        gameManage.gamePlay();
    }
}