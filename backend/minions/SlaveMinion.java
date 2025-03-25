package backend.minions;

import backend.game.HexCell;
import backend.game.InvalidStrategyException;
import backend.game.StrategyEvaluationException;
import backend.game.StrategyProcessingException;
import backend.players.Player;

import java.io.IOException;

public class SlaveMinion extends Minion {

    public SlaveMinion(Player owner, HexCell cell) {
        super(owner, cell);
    }
    @Override
    public void minionStrategy(String strategy) throws IOException, StrategyEvaluationException, StrategyProcessingException, InvalidStrategyException {
        super.minionStrategy(strategy);
    }

    @Override
    public void setPosition(int x, int y) throws IOException {
        super.setPosition(x,y);
    }

    @Override
    public HexCell getPosition() {
        return super.getPosition();
    }

    @Override
    public int getHP(){
        return super.getHP();
    }

    @Override
    public int getDef(){
        return super.getDef();
    }

    @Override
    public int getX(){
        return super.getX();
    }

    @Override
    public int getY(){
        return super.getY();
    }

    @Override
    public Player getOwner(){
        return super.getOwner();
    }

    @Override
    public void setHP(int hp){
        super.setHP(hp);
    }

    @Override
    public void setXY(HexCell newPosition){
        super.setXY(newPosition);
    }

    @Override
    public void addMinion(HexCell hexCell){
        super.addMinion(hexCell);
    }
}