package GameLogic;

public class GameResults {
    private int winningNumber;
    private int winningAmount;

    public GameResults(int winningNumber, int winningAmount) {
        this.winningNumber = winningNumber;
        this.winningAmount = winningAmount;
    }

    public int getWinningNumber() {
        return winningNumber;
    }

    public int getWinningAmount() {
        return winningAmount;
    }
    
}
