import java.io.IOException;

public class GameManage {
    private GameBoard gameBoard;
    public static int turn = 1;
    private int maxTurn = GameRule.MaxTurns;

    public GameManage(){
        gameBoard = GameBoard.getInstance();
    }

    public void gamePlay() throws IOException {
        FileProcess file = new FileProcess();
        file.readConfig("D:\\OOP project\\Configuration");

        while(!isOver()){
            current().takeTurn(turn);
            gameBoard.switchPlayers();
            turn++;
        }
        gameBoard.resetBoard();
        gameOver();
    }

    private boolean isOver(){
        if(opponentMin() == 0 && turn > 2){
            System.out.println("Congratulations! You won!");
            System.out.println(currentName());
            return true;
        }else if(currentMin() == 0 && turn > 2){
            System.out.println("Congratulations! You won!");
            System.out.println(opponentName());
            return true;
        }else if(turn == maxTurn){
            if(currentMin() > opponentMin()){
                System.out.println("Congratulations! You won!");
                System.out.println(currentName());
                return true;
            }else if(currentMin() < opponentMin()){
                System.out.println("Congratulations! You won!");
                System.out.println(opponentName());
                return true;
            }else{
                if(currentHP() > opponentHP()){
                    System.out.println("Congratulations! You won!");
                    System.out.println(currentName());
                    return true;
                }else if(currentHP() < opponentHP()){
                    System.out.println("Congratulations! You won!");
                    System.out.println(opponentName());
                    return true;
                }else{
                    if(currentBudget() > opponentBudget()){
                        System.out.println("Congratulations! You won!");
                        System.out.println(currentName());
                        return true;
                    }else{
                        System.out.println("Congratulations! You won!");
                        System.out.println(opponentName());
                        return true;
                    }
                }
            }
        }else{
            return false;
        }
    }

    private void gameOver() {
        System.out.println("Game has ended.");
        System.exit(0);
    }

    private Player current(){
        return gameBoard.getCurrentPlayer();
    }

    private String currentName(){
        return gameBoard.getCurrentPlayer().getName();
    }

    private String opponentName(){
        return gameBoard.getOpponentPlayer().getName();
    }

    private int currentMin(){
        return gameBoard.getCurrentPlayer().getNumber();
    }

    private int opponentMin(){
        return gameBoard.getOpponentPlayer().getNumber();
    }

    private int currentHP(){
        return gameBoard.getCurrentPlayer().getSumHP();
    }

    private int opponentHP(){
        return gameBoard.getOpponentPlayer().getSumHP();
    }

    private int currentBudget(){
        return gameBoard.getCurrentPlayer().getBudget();
    }

    private int opponentBudget(){
        return gameBoard.getOpponentPlayer().getBudget();
    }
}
